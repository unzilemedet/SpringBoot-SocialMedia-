package com.bilgeadam.config.security;

import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.utility.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//OncePerRequestFilter --> her bir istek için yalnızca bir kez çalışacak bir filtre uygulamanızı sağlayan bir Spring Framework sınıfıdır.
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenManager;

    @Autowired
    private JwtUserDetails jwtUserDetails;

    /**
     * HttpServlet --> Java Servlet API'si tarafından sağlanan bir sınıftır ve HTTP protokolü üzerinden gelen istekleri işlemek için kullanılır.
     * Servletler, dinamik web uygulamaları geliştirmek için kullanılan Java sınıflarıdır ve web sunucusunda çalışır.
     * HTTP protokolüne özgü istekleri (GET, POST, PUT, DELETE vb.) işleyebilme yeteneği sağlar.
     *
     *
     * HttpServletRequest --> Gelen HTTP isteğini temsil eden bir HttpServletRequest nesnesidir.
     *                        Bu nesne, istemci tarafından sunucuya gönderilen isteği içeren bilgileri içerir.
     *                        Örneğin istek başlıkları, parametreler, yol ve HTTP yöntemi.
     *
     * HttpServletResponse --> Cevabı alır.
     * FilterChain --> Filtre zinciri içindeki bir sonraki filtreye veya hedef kaynağa isteği iletmek için kullanılan bir FilterChain nesnesidir.
     *                 Filtre zinciri, isteği işlemek ve yanıt üretmek için bir dizi filtreyi içeren bir yapıdır.
     *
     * Bu parametreler, doFilterInternal metodunun filtreleme işlemini gerçekleştirmek için gelen isteği alması,
     * isteği işlemesi ve yanıtı ileterek devam etmesi için gerekli olan temel bileşenleri temsil eder.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //İstek başlıklarından "Authorization" başlığını alarak bir dize olarak authorizationHeader değişkenine atar.
        //Bu başlık, yetkilendirme için kullanılan JWT'yi içerir.
        final String authorizationHeader=request.getHeader("Authorization");
        System.out.println("Header' a gelen token --> " + authorizationHeader);
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")
        ){
            String token=authorizationHeader.substring(7);
            System.out.println("Header' da substring edilmiş token --> " + token);
            Optional<Long> id=jwtTokenManager.getIdFromToken(token);
            if (id.isPresent()){
                System.out.println("Token'dan alınan id --> " + id);
                //LoadUserByUserId metodunu çağırarak, kullanıcı kimliğine (id) göre kullanıcı ayrıntılarını (UserDetails) alır.
                //Bu ayrıntılar, kullanıcının kimlik bilgilerini, rollerini ve yetkilerini içerir.
                UserDetails userDetails=jwtUserDetails.loadUserByUserId(id.get());
                System.out.println("UserDetails' a gönderilen rol: " + userDetails);
                //Kimlik doğrulama token'ını (authenticationToken) oluşturur.
                //Bu token, kimlik doğrulama işlemlerinde kullanılır.
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //Bu, kimlik doğrulamasının gerçekleştirildiğini ve kimlik bilgilerinin doğru olduğunu belirtir.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
        }
        // Filtre zincirindeki bir sonraki filtreye veya hedef kaynağa isteği iletmek için filterChain'i kullanır.
        // Bu, filtreleme işlemini tamamlar ve sonucunda isteğin devam etmesini sağlar.
        filterChain.doFilter(request,response);

        /**
         * Bu kod parçası, JWT'nin geçerliliğini doğrulamak için kullanıcı kimliği ve rollerini kontrol eder.
         * Geçerli bir JWT varsa, kullanıcının kimlik bilgileri ve yetkileriyle birlikte kimlik doğrulaması gerçekleştirilir
         * ve bu bilgiler güvenlik bağlamına yerleştirilir.
         * Son olarak, filtre zincirindeki bir sonraki adıma geçilir ve istek devam eder.
         */
    }
}

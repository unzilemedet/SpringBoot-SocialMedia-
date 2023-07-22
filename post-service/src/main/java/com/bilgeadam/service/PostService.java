package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateCommentDto;
import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.dto.request.UpdatePostRequestDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.rabbitmq.model.UserProfileResponseModel;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.PostManagerException;
import com.bilgeadam.manager.IUserProfileManager;
import com.bilgeadam.mapper.IPostMapper;
import com.bilgeadam.rabbitmq.model.CreatePostModel;
import com.bilgeadam.rabbitmq.producer.CreatePostProducer;
import com.bilgeadam.repository.IPostRepository;
import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.repository.entity.Like;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService extends ServiceManager<Post, String> {
    private final IPostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserProfileManager userProfileManager;
    private final CreatePostProducer createPostProducer;
    private final LikeService likeService;
    private final CommentService commentService;

    public PostService(IPostRepository postRepository, JwtTokenProvider jwtTokenProvider, IUserProfileManager userProfileManager, CreatePostProducer createPostProducer, LikeService likeService, CommentService commentService) {
        super(postRepository);
        this.postRepository = postRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProfileManager = userProfileManager;
        this.createPostProducer = createPostProducer;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    public Post createPost(String token, CreateNewPostRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
        UserProfileResponseDto userProfile = userProfileManager.findByUserProfileDto(authId.get()).getBody();
        Post post = IPostMapper.INSTANCE.toPost(dto);
        post.setUserId(userProfile.getUserId());
        post.setUsername(userProfile.getUsername());
        post.setAvatar(userProfile.getAvatar());
        return save(post);
    }

    public Post createPostWithRabbitMq(String token, CreateNewPostRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
        CreatePostModel model = CreatePostModel.builder().authId(authId.get()).build();
        UserProfileResponseModel userProfile = (UserProfileResponseModel) createPostProducer.createPost(model);
        Post post = IPostMapper.INSTANCE.toPost(dto);
        post.setId(userProfile.getUserId());
        post.setUsername(userProfile.getUsername());
        post.setAvatar(userProfile.getAvatar());
        return save(post);
    }

    //TODO: content burada set ediliyor, listeye ekleme çıkarma ve content aksiyonlarının mapper üzerinden yönetilmesi yapılacak.
    public Post updatePost(String token, String postId, UpdatePostRequestDto dto){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        authId.orElseThrow(() -> new PostManagerException(ErrorType.INVALID_TOKEN));

        UserProfileResponseDto userProfile = userProfileManager.findByUserProfileDto(authId.get()).getBody();
        Optional<Post> post = postRepository.findById(postId);
        if (userProfile.getUserId().equals(post.get().getUserId())){
            dto.getAddMediaUrls().forEach(x -> post.get().getMediaUrls().add(x));
            //post.get().getMediaUrls().addAll(dto.getAddMediaUrls());
            post.get().getMediaUrls().removeAll(dto.getRemoveMediaUrls());
            post.get().setContent(dto.getContent());
            return update(post.get());
        }
        throw new PostManagerException(ErrorType.POST_NOT_FOUND);
    }

    /*
        Buradaki metot kullanıcının bir postu beğendiği zaman çalışacak metottur.
        Post beğenildiğinde Post' a beğenenin bilgisi gitmelidir.
        Ayrıca tek tek hangi postun kimin tarafından beğenildiğinin bilgisi de Like entity' sinin tablosunda tutulmalıdır.
        - Postu beğenebilmek için giriş yapılmalıdır
        - Postu beğenenin bilgileri gereklidir.

        Bir kullanıcı bir postu iki kez beğenemez. Bunun önüne geçmek için bir kontrol yazılmalıdır.
        Kullanıcı beğendiği bir postta beğen butonuna tekrar basıyorsa beğeni geri alınmalıdır.
     */
    public Boolean likePost(String token, String postId){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
        //tokendan alınan authId bilgisi ile bize lazım olan UserProfile bilgilerinin alınması
        UserProfileResponseDto userProfile = userProfileManager.findByUserProfileDto(authId.get()).getBody();
        //kullanıcının girdiği postId ye göre post bilgilerinin çekilmesi
        Optional<Post> post = findById(postId);

        //bir kişi bir postu beğendiğinde tekrar beğenemeyeceği için beğeniyi geri çekme aksiyonu
        Optional<Like> optionalLike = likeService.findByUserIdAndPostId(userProfile.getUserId(), postId);
        if (optionalLike.isPresent()){
            post.get().getLikes().remove(userProfile.getUserId());
            update(post.get());
            likeService.delete(optionalLike.get());
            return true;
        }else {
            Like like = IPostMapper.INSTANCE.toLike(userProfile);
            like.setPostId(postId);
            likeService.save(like);

            if (post.isEmpty()){
                //hata düzeltilecek
                throw new PostManagerException(ErrorType.POST_NOT_FOUND);
            }
            post.get().getLikes().add(userProfile.getUserId());
            update(post.get());
            return true;
        }
    }

    public Boolean deletePost(String token, String postId){
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()){
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
        UserProfileResponseDto userProfile = userProfileManager.findByUserProfileDto(authId.get()).getBody();
        Optional<Post> post = findById(postId);
        if (post.get().getUserId().equals(userProfile.getUserId())){
            //like silme
            post.get().getLikes().forEach(x -> likeService.deleteByUserIdAndPostId(x, postId));
            //post silme
            deleteById(postId);
            return true;
        }else {
            //TODO: hata tipi değiştirilecek
            throw new PostManagerException(ErrorType.POST_NOT_FOUND);
        }
    }

    public Boolean createComment(String token, CreateCommentDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new PostManagerException(ErrorType.INVALID_TOKEN);
        }
        UserProfileResponseDto userProfile = userProfileManager.findByUserProfileDto(authId.get()).getBody();
        Optional<Post> post = findById(dto.getPostId());
        if (post.isPresent()) {
            Comment comment = Comment.builder()
                    .userId(userProfile.getUserId())
                    .postId(dto.getPostId())
                    .username(userProfile.getUsername())
                    .comment(dto.getComment())
                    .build();
            commentService.save(comment);
            if (dto.getCommentId() != null){
                Optional<Comment> optionalComment = commentService.findById(dto.getCommentId());
                if (optionalComment.isEmpty()){
                    //TODO exception yazılacak
                    throw new PostManagerException(ErrorType.POST_NOT_FOUND);
                }
                optionalComment.get().getSubCommentId().add(comment.getId());
                commentService.update(optionalComment.get());
            }
            post.get().getComments().add(comment.getId());
            save(post.get());
            return true;
        }
        throw new PostManagerException(ErrorType.POST_NOT_FOUND);
    }
}

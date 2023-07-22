package com.bilgeadam.service;

import com.bilgeadam.mapper.IElasticMapper;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {

    private final IUserProfileRepository userProfileRepository;
    public UserProfileService(ElasticsearchRepository<UserProfile, String> repository, IUserProfileRepository userProfileRepository) {
        super(repository);
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile createUserWithRabbitMq(RegisterElasticModel registerElasticModel){
        return save(IElasticMapper.INSTANCE.fromElasticToUserProfile(registerElasticModel));
    }

    //elasticsearch' de metinsel(String vb.) değerler üzerinde herhangi bir sıralama işlemi yapılamamaktadır.
    //Bu işlemin gerçekleşebilmesi için elasticsearch arayüzüne kibana vb. araçlar ile ulaşarak bazı index ayarlarının
    //yapılması gerekmektedir. Burada sıralama işlemleri sayısal değerler üzerinde yapılabilmektedir.
    public Page<UserProfile> findAll(int page, int size, String sortParameter, String sortType){
        Pageable pageable = null;
        Sort sort = null;
        if(sortParameter != null){
            sort = Sort.by(Sort.Direction.fromString(sortType == null ? "ASC" : sortType), sortParameter);
        }if (sort != null){
            pageable = PageRequest.of(page, size == 0 ? 10 : size, sort);
        }else {
            pageable = PageRequest.of(page, size == 0 ? 10 : size);
        }
        return userProfileRepository.findAll(pageable);
    }

    public void deleteAll(){
        userProfileRepository.deleteAll();
    }
}

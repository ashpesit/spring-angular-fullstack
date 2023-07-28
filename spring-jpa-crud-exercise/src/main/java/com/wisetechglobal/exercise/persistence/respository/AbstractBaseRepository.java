package com.wisetechglobal.exercise.persistence.respository;

import com.wisetechglobal.exercise.persistence.model.AbstractBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AbstractBaseRepository<T extends AbstractBaseEntity, I extends Serializable> extends JpaRepository<T, I> {
    
}

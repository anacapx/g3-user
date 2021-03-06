package com.g3.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.g3.user.dao.UserDao;
import com.g3.user.exception.customException.CpfOrEmailInUseException;
import com.g3.user.exception.customException.UserNotFoundException;
import com.g3.user.model.User;
import com.g3.user.service.interfaces.IUserService;


@Service
public class UserService implements IUserService{

    @Autowired
    UserDao dao;

    public User register(User user) throws CpfOrEmailInUseException {
        if(!dao.findByCpf(user.getCpf()).isEmpty()){
            throw new CpfOrEmailInUseException();
        }

        if(!dao.findByEmail(user.getEmail()).isEmpty()){
            throw new CpfOrEmailInUseException();
        }

        return dao.save(user);
    }

    public List<User> getAll(int page, int size){
    	Pageable pageable = PageRequest.of(page, size);
    	Page<User> usersPage = dao.findAll(pageable);
    	List<User> usersList = new ArrayList<>();
		for (User user : usersPage) {
			usersList.add(user);
		}
        return usersList;
    }

    public User update(User newUserData, Long userId) throws UserNotFoundException {
        if (dao.findById(userId).isEmpty()){
            throw new UserNotFoundException();
        }
        User user = dao.findById(userId).get();

        user.setBirthday(newUserData.getBirthday());
        user.setName(newUserData.getName());
        user.setCpf(newUserData.getCpf());
        user.setEmail(newUserData.getEmail());
        user.setPhone(newUserData.getPhone());

        dao.save(user);

        return user;
    }

    public void delete(Long id) throws UserNotFoundException {
        if (dao.findById(id).isEmpty()){
            throw new UserNotFoundException();
        }
        dao.deleteById(id);
    }

    public User searchById(Long id) throws UserNotFoundException {
        if (dao.findById(id).isEmpty()){
            throw new UserNotFoundException();
        }
        return dao.findById(id).get();
    }

    public List<User> searchByName(String name){
    	List<User> usersByName = dao.getUsersByName(name);
    	System.out.println(usersByName);
    	if (usersByName.isEmpty()){
            throw new UserNotFoundException();
        }
        return usersByName;
    }

    public List<User> searchByCPF(String cpf){
    	List<User> usersByCPF = dao.findByCpfContaining(cpf);
    	if (usersByCPF.isEmpty()){
            throw new UserNotFoundException();
        }
        return usersByCPF;
    }

    public List<User> searchByEmail(String email){
    	List<User> usersByEmail = dao.findByEmailContainingIgnoreCase(email);
    	if (usersByEmail.isEmpty()){
            throw new UserNotFoundException();
        }
        return usersByEmail;
    }
}

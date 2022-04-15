package com.codingdojo.lino.servicios;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.lino.modelos.LoginUser;
import com.codingdojo.lino.modelos.User;
import com.codingdojo.lino.repositorios.UserRepository;

@Service
public class appService {

	@Autowired
	private UserRepository repositorio_user;
	
	
	//Create a new user
	public User register(User nuevoUsuario, BindingResult result) {
		
		String nuevoEmail = nuevoUsuario.getEmail();
		//check if the email exists in the database
		if(repositorio_user.findByEmail(nuevoEmail).isPresent()) {
			result.rejectValue("email", "Unique", "el correo fue ingresado previamente");
		}
		
		if(! nuevoUsuario.getPassword().equals(nuevoUsuario.getConfirm())){
			result.rejectValue("confirm",  "Matches", "Las contraseñas no coinciden");
		}
		if(result.hasErrors()) {
			return null;
		}else {
			//Encriptamos contraseña
			String contra_encr = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
			nuevoUsuario.setPassword(contra_encr);
			//Guardo usuario
			return repositorio_user.save(nuevoUsuario);
		}
	}
	
	
	
	public User login(LoginUser nuevoLogin, BindingResult result) {
		
		if(result.hasErrors()) {
			return null;
		}
		
		//Find by email
		Optional<User> posibleUsuario = repositorio_user.findByEmail(nuevoLogin.getEmail());
		if(!posibleUsuario.isPresent()) {
			result.rejectValue("email", "Unique", "Correo ingresado no existe");
			return null;
		}
		
		User user_login = posibleUsuario.get();
		
		//Comparamos contraseñas encriptadas
		if(!BCrypt.checkpw(nuevoLogin.getPassword(), user_login.getPassword())){
			result.rejectValue("password", "Matches", "Contraseña Inválida");
		}
		
		if(result.hasErrors()) {
			return null;
		}else {
			return user_login;
		}
	
	}
	
}

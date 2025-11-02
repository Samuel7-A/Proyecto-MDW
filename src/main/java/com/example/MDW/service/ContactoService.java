package com.example.MDW.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MDW.Repositorio.ContactoRepository;
import com.example.MDW.model.Contacto;

@Service
public class ContactoService {
    @Autowired
    ContactoRepository contactorepo;

    public void guardarContacto(Contacto contacto) {
        contactorepo.save(contacto);
    }

}

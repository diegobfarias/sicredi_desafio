package com.sicredi_desafio.diegobfarias.services.exceptions;

public class CpfNotFoundException extends RuntimeException {
    public CpfNotFoundException(String message) {
        super(message);
    }
}

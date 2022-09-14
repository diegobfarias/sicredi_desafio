package com.sicredi_desafio.diegobfarias.services.exceptions;

public class AssociateAlreadyVotedException extends RuntimeException {
    public AssociateAlreadyVotedException(String associateId) {
        super(associateId);
    }
}

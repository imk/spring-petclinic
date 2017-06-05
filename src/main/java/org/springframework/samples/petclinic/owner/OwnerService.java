package org.springframework.samples.petclinic.owner;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codahale.metrics.annotation.Timed;

@Service
public class OwnerService {
    private final OwnerRepository owners;


    @Autowired
    public OwnerService(OwnerRepository clinicService) {
        this.owners = clinicService;
    }


    @Timed
	public Collection<Owner> findByLastName(String lastName) {
		return owners.findByLastName(lastName);
	}

    @Timed
	public Owner findById(Integer id) {
		return owners.findById(id);
	}

    @Timed
	public void save(Owner owner) {
		owners.save(owner);
	}

}

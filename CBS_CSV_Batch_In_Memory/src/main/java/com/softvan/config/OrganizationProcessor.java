package com.softvan.config;

import com.google.gson.Gson;
import com.softvan.entity.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class OrganizationProcessor implements ItemProcessor<Organization, Organization> {


    @Override
    public Organization process(Organization organization) {
        System.out.println("data ::" + new Gson().toJson(organization));
        return organization;
    }
}

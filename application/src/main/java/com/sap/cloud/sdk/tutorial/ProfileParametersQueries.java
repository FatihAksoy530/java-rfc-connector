package com.sap.cloud.sdk.tutorial;

import javax.persistence.*;
import javax.transaction.Transactional;

public class ProfileParametersQueries {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("JEETut3");

    public static void main(String[] args) {

        ENTITY_MANAGER_FACTORY.close();
    }

    public static void addParameter(String parameter_name, String user_value, String default_value, String description, String company_subdomain) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {

            et = em.getTransaction();
            et.begin();
            ProfileParameters parameter = new ProfileParameters();
            parameter.setParameterName(parameter_name);
            parameter.setUserValue(user_value);
            parameter.setDefaultValue(default_value);
            parameter.setDescription(description);
            parameter.setCompanySubdomain(company_subdomain);
            em.persist(parameter);
            et.commit();
        }
        catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        }
        finally {
            em.close();
        }

    }

    public static void deleteParameters(String company_subdomain) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            String query = "DELETE FROM ProfileParameters p WHERE p.company_subdomain = :company_subdomain";
            Query q = em.createQuery(query);
            q.setParameter("company_subdomain", company_subdomain);
            q.executeUpdate();
            et.commit();
        }
        catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
    }

}

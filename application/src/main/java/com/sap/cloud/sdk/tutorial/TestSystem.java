package com.sap.cloud.sdk.tutorial;

import javax.persistence.*;

public class TestSystem {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
            .createEntityManagerFactory("JEETut3");

  public static void main(String[] args) {

      ENTITY_MANAGER_FACTORY.close();
  }

  public static void addAccount(String user_name) {
      EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
      EntityTransaction et = null;
      try {
          et = em.getTransaction();
          et.begin();
          Accounts account = new Accounts();
          account.setName(user_name);
          em.persist(account);
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
    public static void getAccount(String user_name) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT a FROM Accounts a WHERE a.user_name = :user_name";

        TypedQuery<Accounts> q = em.createQuery(query, Accounts.class);
        q.setParameter("user_name", user_name);
        Accounts account = null;
        try {
            account = q.getSingleResult();
            System.out.println("Account found: " + account.getName());
        }
        catch (NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public static void getAllAccounts() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT a FROM Accounts a";

        TypedQuery<Accounts> q = em.createQuery(query, Accounts.class);
        try {
            for (Accounts account : q.getResultList()) {
                System.out.println("Account found: " + account.getName());
            }
        }
        catch (NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
    }

    public static void updateAccount(String user_name, String new_name) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            String query = "SELECT a FROM Accounts a WHERE a.user_name = :user_name";
            TypedQuery<Accounts> q = em.createQuery(query, Accounts.class);
            q.setParameter("user_name", user_name);
            Accounts account = q.getSingleResult();
            account.setName(new_name);
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

    public static void deleteAccount(String user_name) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();
            String query = "SELECT a FROM Accounts a WHERE a.user_name = :user_name";
            TypedQuery<Accounts> q = em.createQuery(query, Accounts.class);
            q.setParameter("user_name", user_name);
            Accounts account = q.getSingleResult();
            em.remove(account);
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

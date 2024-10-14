package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      javax.persistence.EntityManager em = sessionFactory.getCurrentSession().getEntityManagerFactory().createEntityManager();
      javax.persistence.EntityGraph<?> entityGraph = em.createEntityGraph(User.class);
      entityGraph.addAttributeNodes("car");

      TypedQuery<User> query = em.createQuery("from User", User.class);
      query.setHint("javax.persistence.fetchgraph", entityGraph);

      return query.getResultList();
   }

   @Override
   @SuppressWarnings("unchecked")
   public User getUserByCar(String model, int series) {
      javax.persistence.EntityManager em = sessionFactory.getCurrentSession().getEntityManagerFactory().createEntityManager();
      javax.persistence.EntityGraph<?> entityGraph = em.createEntityGraph(User.class);
      entityGraph.addAttributeNodes("car");

      String jpql = "from User u where u.car.model = :model and u.car.series = :series";
      TypedQuery<User> query = em.createQuery(jpql, User.class);
      query.setParameter("model", model);
      query.setParameter("series", series);
      query.setHint("javax.persistence.fetchgraph", entityGraph);

      return query.setMaxResults(1).getSingleResult();
   }

}

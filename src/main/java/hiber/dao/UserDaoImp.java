package hiber.dao;

import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   public List<User> listUsersWithCar(String model, int series) {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("select users from User users join users.car car where car.model = :carModel and car.series = :carSeries", User.class);
      query.setParameter("carModel", model);
      query.setParameter("carSeries", series);
      return query.getResultList();
   }


   public void removeUserById2(long id) {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         User user = session.get(User.class, id);
         if (user != null) {
            session.remove(user);
         }
         transaction.commit();
         System.out.println("Пользователь успешно удален");
      } catch (Exception e) {
         if (transaction != null && transaction.isActive()) {
            transaction.rollback();}
         throw new RuntimeException("Возникла ошибка при удаления пользователя");
      }
   }

   public List<User> getAllUsers() {
      Query<User> query=sessionFactory.getCurrentSession().createQuery("from User", User.class);
      return query.list();
   }

}

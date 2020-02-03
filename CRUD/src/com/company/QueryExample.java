package com.company;

import com.company.entity.Employee;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.util.Iterator;
import java.util.List;

public class QueryExample {

    private static SessionFactory factory;
    private static ServiceRegistry registry;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // factory = new Configuration().configure().buildSessionFactory();
            Configuration configuration = new Configuration().configure();
//            registry = new StandardServiceRegistryBuilder().applySettings(
//                    configuration.getProperties()).build();
            registry = new StandardServiceRegistryBuilder().
                    configure()
                    .build();
//            factory = configuration.buildSessionFactory(registry);
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        //HQL Examples
        Session session = factory.openSession();
        //Transaction tx = null;

        try {
            //tx = session.beginTransaction();
//            String hql = "SELECT max(e.salary) FROM Employee  as e";
//            Query query = session.createQuery(hql);
////            query.setInteger("salary", 10000);
////            Query query = session.createQuery("from Employee as e where e.firstName "
////                    + "like 'M%' and salary > 10000");
////            Query query = session.createQuery("select e.firstName from Employee as e");
//            int s = (int) query.uniqueResult();
//            System.out.println("Max slary = " + s);
////            List employees = query.list();

            Criteria criteria = session.createCriteria(Employee.class);

            criteria.add(Restrictions.gt("salary", 100));

            criteria.addOrder(Order.asc("firstName"));

            List employees = criteria.list();


            for (Iterator iterator =
                 employees.iterator(); iterator.hasNext(); ) {
                Employee ee = (Employee) iterator.next();
//                System.out.println(ee);
                System.out.print("First Name: " + ee.getFirstName());
                System.out.print("  Last Name: " + ee.getLastName());
                System.out.println("  Salary: " + ee.getSalary());
            }


        } catch (HibernateException e) {
            // if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }


        StandardServiceRegistryBuilder.destroy(registry);

    }

}
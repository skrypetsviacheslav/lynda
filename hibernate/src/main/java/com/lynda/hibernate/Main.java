package com.lynda.hibernate;

import com.lynda.hibernate.entity.Message;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static SessionFactory factory;
    private static ServiceRegistry registry;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String message = "";
        System.out.println("Enter a message: ");
        message = in.nextLine();

        try {
            Configuration conf = new Configuration().configure();
//            registry = new StandardServiceRegistryBuilder().applySettings(
//                    conf.getProperties()).build();
            registry = new StandardServiceRegistryBuilder().
                    configure()
                    .build();
//            factory = conf.buildSessionFactory(registry);
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Throwable e) {
            System.out.println("Fail to create session factory object" + e);
            throw new ExceptionInInitializerError(e);
        }
        Session session = factory.openSession();
        Transaction tx = null;
        Short msgId = null;
        try {
            tx = session.beginTransaction();
            Message msg = new Message(message);
            msgId = (Short) session.save(msg);

            List messages = session.createQuery("FROM Message").list();

            for (Iterator iterator = messages.iterator(); iterator.hasNext(); ) {
                Message m = (Message) iterator.next();
                System.out.println("message: " + m.getMessage());
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        StandardServiceRegistryBuilder.destroy(registry);
    }
}

package com.projects.phone_contacts.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_contact_name", columnNames = {"firstName", "middleName", "lastName"})
})
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "contact",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Email> emails;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "contact",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<Phone> phones;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    private String imagePath;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy ?
                hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy ?
                hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Contacts contact = (Contacts) o;
        return getId() != null && Objects.equals(getId(), contact.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
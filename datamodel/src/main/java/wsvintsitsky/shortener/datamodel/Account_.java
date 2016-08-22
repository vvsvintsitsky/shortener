package wsvintsitsky.shortener.datamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Account.class)
public class Account_ extends AbstractModel_ {

	public static volatile SingularAttribute<Account, String> email;
	public static volatile SingularAttribute<Account, String> password;
	public static volatile SingularAttribute<Account, Date> created;
	public static volatile SingularAttribute<Account, Boolean> isNotified;
	public static volatile SingularAttribute<Account, Boolean> isConfirmed;
	public static volatile ListAttribute<Account, Url> urls;
	
}

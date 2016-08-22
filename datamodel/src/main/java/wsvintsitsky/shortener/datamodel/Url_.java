package wsvintsitsky.shortener.datamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Url.class)
public class Url_ extends AbstractModel_ {

	public static volatile SingularAttribute<Url, String> shortUrl;
	public static volatile SingularAttribute<Url, String> longUrl;
	public static volatile SingularAttribute<Url, Long> visited;
	public static volatile SingularAttribute<Url, String> description;
	public static volatile SingularAttribute<Url, Account> account;
	public static volatile ListAttribute<Url, Tag> tags;
}

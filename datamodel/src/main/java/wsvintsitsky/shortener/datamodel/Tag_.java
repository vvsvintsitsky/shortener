package wsvintsitsky.shortener.datamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tag.class)
public class Tag_ extends AbstractModel_ {

	public static volatile SingularAttribute<Tag, String> description;
	public static volatile ListAttribute<Tag, Url> urls;
}

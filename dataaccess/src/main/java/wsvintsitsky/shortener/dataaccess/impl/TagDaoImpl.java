package wsvintsitsky.shortener.dataaccess.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import wsvintsitsky.shortener.dataaccess.TagDao;
import wsvintsitsky.shortener.datamodel.Tag;
import wsvintsitsky.shortener.datamodel.Tag_;

@Repository
public class TagDaoImpl extends AbstractDaoImpl<Tag, Long> implements TagDao {

	protected TagDaoImpl() {
		super(Tag.class);
	}

	@Override
	public Tag getTagWithUrls(Long id) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
		Root<Tag> from = cq.from(Tag.class);
		
		from.fetch(Tag_.urls, JoinType.LEFT);
		Predicate idCondition = cb.equal(from.get(Tag_.id), id);
		cq.where(idCondition);
		
		TypedQuery<Tag> q = em.createQuery(cq);
		return q.getSingleResult();
	}
	
}

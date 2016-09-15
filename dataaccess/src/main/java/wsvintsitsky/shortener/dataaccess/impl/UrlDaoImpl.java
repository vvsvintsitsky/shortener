package wsvintsitsky.shortener.dataaccess.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import wsvintsitsky.shortener.dataaccess.UrlDao;
import wsvintsitsky.shortener.datamodel.Account_;
import wsvintsitsky.shortener.datamodel.Url;
import wsvintsitsky.shortener.datamodel.Url_;

@Repository
public class UrlDaoImpl extends AbstractDaoImpl<Url, Long> implements UrlDao {

	protected UrlDaoImpl() {
		super(Url.class);
	}

	@Override
	public Url getUrlWithTags(String shortUrl) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Url> cq = cb.createQuery(Url.class);
		Root<Url> from = cq.from(Url.class);

		from.fetch(Url_.tags, JoinType.LEFT);
		Predicate shortUrlCondition = cb.equal(from.get(Url_.shortUrl), shortUrl);
		cq.where(shortUrlCondition);

		TypedQuery<Url> q = em.createQuery(cq);
		List<Url> urls = q.getResultList();
		if (urls.size() == 1) {
			return urls.get(0);
		} else if (urls.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("More than one url found");
		}
	}

	@Override
	public Url getUrlByShortUrl(String shortUrl) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Url> cq = cb.createQuery(Url.class);
		Root<Url> from = cq.from(Url.class);

		Predicate shortUrlCondition = cb.equal(from.get(Url_.shortUrl), shortUrl);
		cq.where(shortUrlCondition);

		TypedQuery<Url> q = em.createQuery(cq);
		List<Url> urls = q.getResultList();
		if (urls.size() == 1) {
			return urls.get(0);
		} else if (urls.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("More than one url found");
		}
	}

	@Override
	public List<Url> getUrlsByAccountId(Long accountId) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Url> cq = cb.createQuery(Url.class);
		Root<Url> from = cq.from(Url.class);

		Predicate accountIdCondition = cb.equal(from.get(Url_.account).get(Account_.id), accountId);
		cq.where(accountIdCondition);

		TypedQuery<Url> q = em.createQuery(cq);
		return q.getResultList();
	}

	@Override
	public Url checkOwnership(Long accountId, String shortUrl) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Url> cq = cb.createQuery(Url.class);
		Root<Url> from = cq.from(Url.class);

		Predicate accountIdCondition = cb.equal(from.get(Url_.account).get(Account_.id), accountId);
		Predicate shortUrlCondition = cb.equal(from.get(Url_.shortUrl), shortUrl);
		Predicate condition = cb.and(accountIdCondition, shortUrlCondition);
		cq.where(condition);

		TypedQuery<Url> q = em.createQuery(cq);
		List<Url> urls = q.getResultList();

		if (urls.size() == 1) {
			return urls.get(0);
		} else if (urls.size() == 0) {
			return null;
		} else {
			throw new IllegalStateException("More than one url found");
		}
	}

}

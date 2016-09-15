package wsvintsitsky.shortener.dataaccess.impl;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import wsvintsitsky.shortener.dataaccess.AccountDao;
import wsvintsitsky.shortener.datamodel.Account;
import wsvintsitsky.shortener.datamodel.Account_;

@Repository
public class AccountDaoImpl extends AbstractDaoImpl<Account, Long> implements AccountDao {

	protected AccountDaoImpl() {
		super(Account.class);
	}

	@Override
	public Account getByEmailAndPassword(String email, String password) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> from = cq.from(Account.class);
		
		Predicate emailCondition = cb.equal(from.get(Account_.email), email);
		Predicate passwordCondition = cb.equal(from.get(Account_.password), password);
		cq.where(cb.and(emailCondition, passwordCondition));
		
		TypedQuery<Account> q = em.createQuery(cq);
		List<Account> accounts = q.getResultList();
		if(accounts.size() == 0) {
			return null;
		} else if (accounts.size() == 1) {
			return accounts.get(0);
		} else {
			throw new IllegalStateException("more than 1 account found");
		}
	}

	@Override
	public List<Account> findNotNotified() {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Account> cq = cb.createQuery(Account.class);
		Root<Account> from = cq.from(Account.class);
		
		Predicate isNotifiedCondition = cb.equal(from.get(Account_.isNotified), false);
		cq.where(isNotifiedCondition);
		
		TypedQuery<Account> q = em.createQuery(cq);
		return q.getResultList();
	}

	@Override
	public void deleteNotConfirmed(Date date) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<Account> cd = cb.createCriteriaDelete(Account.class);
		Root<Account> from = cd.from(Account.class);
		
		Predicate isNotConfirmedCondition = cb.equal(from.get(Account_.isConfirmed), false);
		Predicate dateCondition = cb.lessThanOrEqualTo(from.get(Account_.created), date);
		cd.where(cb.and(isNotConfirmedCondition, dateCondition));
		
		em.createQuery(cd).executeUpdate();
	}

}

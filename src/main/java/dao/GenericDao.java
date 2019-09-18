package dao;

import java.lang.reflect.ParameterizedType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class GenericDao<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	private Class<T> classe;

	protected CriteriaBuilder cb;
	protected CriteriaQuery<T> q;
	protected Root<T> root;

	@SuppressWarnings("unchecked")
	public GenericDao() {
		try {
			this.classe = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	protected void prepareBuildQuery() {
		this.cb = entityManager.getCriteriaBuilder();
		this.q = cb.createQuery(classe);
		this.root = q.from(classe);
	}

	public T findById(Integer id) {
		try {
			T retorno = (T) entityManager.find(classe, id);
			return retorno;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void remove(T obj) {
		try {
			entityManager.remove(obj);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public T merge(T obj) {
		try {
			T result = entityManager.merge(obj);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void persist(T obj) {
		try {
			entityManager.persist(obj);
		} catch (Exception re) {
			throw re;
		}
	}

}

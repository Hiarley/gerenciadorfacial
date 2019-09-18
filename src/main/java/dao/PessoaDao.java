package dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import dominio.Pessoa;
import dominio.lazy.PessoaLazyFilter;

@Stateless
public class PessoaDao extends GenericDao<Pessoa>{
	@PersistenceContext
	private EntityManager em;
	
	public PessoaDao() {
		// TODO Auto-generated constructor stub
	}

	public Pessoa findByCPF(String cpf) {
		prepareBuildQuery();
		q.select(root);
		q.where(cb.equal(root.get("cpf"), cpf));
		try {
			Pessoa p = (Pessoa) this.em.createQuery(q).getSingleResult();
			return p;
		} catch (NoResultException e) {
			return null;
		}
	}

	public int quantidadeFiltrados(PessoaLazyFilter filtro) {
		try {
			prepareBuildQuery();
			CriteriaQuery<Long> query = cb.createQuery(Long.class);
			root = query.from(Pessoa.class);
			query.select(cb.count(root));
			Long rs = em.createQuery(query).getSingleResult();
					
			try {
				return rs.intValue();
			} catch (NoResultException e) {
				return 0;
			}

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public List<Pessoa> filtrados(PessoaLazyFilter filtro) {
		try {
			prepareBuildQuery();
			
			Predicate predicate = cb.and();

			CriteriaQuery<Pessoa> queryPaginacao = q.select(root).where(predicate);

			TypedQuery<Pessoa> query = em.createQuery(queryPaginacao);
			query.setFirstResult(filtro.getPrimeiroRegistro());
			query.setMaxResults(filtro.getQuantidadeRegistros());

			List<Pessoa> list = query.getResultList();
			return list;

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}	}

	public List<Pessoa> findAllCpf() {
		prepareBuildQuery();
		q.multiselect(root.get("id"), root.get("cpf"));
		return this.em.createQuery(q).getResultList();
	}
	
}

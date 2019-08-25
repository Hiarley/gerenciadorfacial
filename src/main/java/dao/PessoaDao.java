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

//			if (filtro.getFindBy().values() != null) {
//				Map<String, Object> filters = filtro.getFindBy();
//
//				for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
//					String filterPropriedade = it.next();
//					Object filterValor = filters.get(filterPropriedade);
//					if (filterValor != null) {
//						if (filterPropriedade.equals("dtSolicitacao")) {
//
//							predicate = builder.and(predicate,
//									builder.like(from.get("dtSolicitacao"), "%" + filterValor.toString() + "%"));
//						} else {
//							predicate = builder.and(predicate,
//									builder.like(from.get(filterPropriedade), "%" + filterValor.toString() + "%"));
//						}
//					}
//
//				}
//
//			}

			CriteriaQuery<Pessoa> queryPaginacao = q.select(root).where(predicate);

//			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
//				if (filtro.getPropriedadeOrdenacao().equals("dtSolicitacao")) {
//					queryPaginacao.orderBy(cb.desc(from.get("dtSolicitacao")));
//
//				} else if (filtro.getPropriedadeOrdenacao().equals("stProtocolo")) {
//					queryPaginacao.orderBy(builder.desc(from.get("id")));
//				} else {
//					queryPaginacao.orderBy(builder.desc(from.get(filtro.getPropriedadeOrdenacao())));
//				}
//			} else if (filtro.getPropriedadeOrdenacao() != null) {
//				if (filtro.getPropriedadeOrdenacao().equals("dtSolicitacao")) {
//					queryPaginacao.orderBy(builder.asc(from.get("dtSolicitacao")));
//				} else if (filtro.getPropriedadeOrdenacao().equals("stProtocolo")) {
//
//					queryPaginacao.orderBy(builder.asc(from.get("id")));
//				} else {
//
//					queryPaginacao.orderBy(builder.asc(from.get(filtro.getPropriedadeOrdenacao())));
//				}
//			} else {
//				queryPaginacao.orderBy(builder.asc(from.get("id")));
//			}
			TypedQuery<Pessoa> query = em.createQuery(queryPaginacao);
			query.setFirstResult(filtro.getPrimeiroRegistro());
			query.setMaxResults(filtro.getQuantidadeRegistros());

			List<Pessoa> list = query.getResultList();
			return list;

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}	}
	
}

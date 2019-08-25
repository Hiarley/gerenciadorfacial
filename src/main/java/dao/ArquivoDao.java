package dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dominio.Arquivo;

@Stateless
public class ArquivoDao extends GenericDao<Arquivo>{
	@PersistenceContext
	private EntityManager em;
	
	public ArquivoDao() {
		// TODO Auto-generated constructor stub
	}
	
	
}

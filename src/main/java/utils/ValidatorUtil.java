package utils;

import java.util.Collection;
import java.util.Map;

public class ValidatorUtil {

	/**
	 * Valida se string é diferente de null e não é vazia.
	 *
	 * @return
	 */
	public static final boolean isEmpty(String s) {
		return (s == null || s.trim().length() == 0);
	}

	/**
	 * Retorna TRUE se TODOS os objetos informados estão vazios.
	 * 
	 * @param objs
	 * @return
	 */
	public static final boolean isAllEmpty(Object... objs) {

		if (objs == null)
			return true;

		for (Object o : objs) {
			if (isEmpty(o) == false)
				return false;
		}

		return true;
	}

	/**
	 * Retorna TRUE se todos os objetos estão populados.
	 * 
	 * @param objs
	 * @return
	 */
	public static final boolean isAllNotEmpty(Object... objs) {

		if (objs == null)
			return false;

		for (Object o : objs) {
			if (isEmpty(o) == true)
				return false;
		}

		return true;
	}

	/**
	 * Valida se um objeto é vazio. O seu funcionamento vai depender do tipo de
	 * objeto passado como parêmetro. Se o objeto for nulo, é vazio. Se for uma
	 * string, verifica se não é string vazia ou não é formada apenas por espaços. Se
	 * for uma coleção, verifica se a coleção esta vazia, etc.
	 *
	 * @return
	 */
	public static final boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			return isEmpty((String) o);
		}
		if (o instanceof Number) {
			Number i = (Number) o;
			return (i.doubleValue() == 0.0);
		}
		if (o instanceof Object[]) {
			return ((Object[]) o).length == 0;
		}
		if (o instanceof int[]) {
			return ((int[]) o).length == 0;
		}
		if (o instanceof Collection<?>) {
			return ((Collection<?>) o).size() == 0;
		}
		if (o instanceof Map<?, ?>) {
			return ((Map<?, ?>) o).size() == 0;
		}
		return false;
	}

	/**
	 * Verifica se um objeto não está vazio. é a negação do método isEmpty.
	 * 
	 * @param o
	 * @return
	 */
	public static final boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

}

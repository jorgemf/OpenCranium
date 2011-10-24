package opencranium.data;

import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * Description of the percept, it contains the name of the percept, a set of
 * strings that represents symbolic information and a set of pairs name value
 * which represents information with meaning. In case of the pairs name-value
 * the value should be normalized between -1 and 1 in order to get a correct
 * similarity value between descriptions.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class PerceptDescription {

	/**
	 * Name of the percept.
	 */
	private final String name;

	/**
	 * Set of strings that represents the symbolic information of the percept.
	 */
	private final SortedSet<String> symbolicDataRepresentation;

	/**
	 * Set of pairs name value which represents information with meaning.
	 */
	private final SortedMap<String, Float> subsymbolicDataRepresentation;

	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            Name of the percept.
	 * @param symbolicDataRepresentation
	 *            Set of strings that represents symbolic information
	 * @param subsymbolicDataRepresentation
	 *            Set of pairs name value which represents information with
	 *            meaning. The values should be normalized between 0 and 1 in
	 *            order to get the correct results in the similarity method.
	 */
	public PerceptDescription(String name, SortedSet<String> symbolicDataRepresentation,
			SortedMap<String, Float> subsymbolicDataRepresentation) {
		this.name = name;
		this.symbolicDataRepresentation = symbolicDataRepresentation;
		this.subsymbolicDataRepresentation = subsymbolicDataRepresentation;
	}

	/**
	 * Calculates the similarity of this percept description with another one.
	 * The method uses the cosine similarity. The resulting similarity ranges
	 * from −1 meaning exactly opposite, to 1 meaning exactly the same, with 0
	 * usually indicating independence, and in-between values indicating
	 * intermediate similarity or dissimilarity.
	 * 
	 * @param perceptDescription
	 *            Another percept description.
	 * @return a value between -1 and 1. −1 means exactly opposite, 1 means
	 *         exactly the same and 0 indicates independence.
	 */
	public float similarity(PerceptDescription perceptDescription) {
		float aux;
		double moduleA = 0;
		if (this.symbolicDataRepresentation != null) {
			moduleA += this.symbolicDataRepresentation.size();
		}
		if (this.subsymbolicDataRepresentation != null) {
			for (Entry<String, Float> entry : this.subsymbolicDataRepresentation.entrySet()) {
				aux = entry.getValue();
				moduleA += aux * aux;
			}
		}
		moduleA = Math.sqrt(moduleA);

		double moduleB = 0;
		if (perceptDescription.symbolicDataRepresentation != null) {
			moduleB += perceptDescription.symbolicDataRepresentation.size();
		}
		if (perceptDescription.subsymbolicDataRepresentation != null) {
			for (Entry<String, Float> entry : perceptDescription.subsymbolicDataRepresentation.entrySet()) {
				aux = entry.getValue();
				moduleB += aux * aux;
			}
		}
		moduleB = Math.sqrt(moduleB);

		float result = 0;
		double module = moduleA * moduleB;

		if (module > 0) {

			int symbolicValue = 0;

			Set<String> minSet = this.symbolicDataRepresentation;
			Set<String> maxSet = perceptDescription.symbolicDataRepresentation;
			if (minSet != null && maxSet != null && minSet.size() > 0 && maxSet.size() > 0) {
				if (this.symbolicDataRepresentation.size() > perceptDescription.symbolicDataRepresentation.size()) {
					minSet = perceptDescription.symbolicDataRepresentation;
					maxSet = this.symbolicDataRepresentation;
				}
				for (String s : minSet) {
					if (maxSet.contains(s)) {
						symbolicValue++;
					}
				}
			}

			float subsymbolicValue = 0;

			SortedMap<String, Float> minSortedSet = this.subsymbolicDataRepresentation;
			SortedMap<String, Float> maxSortedSet = perceptDescription.subsymbolicDataRepresentation;
			if (minSortedSet != null && maxSortedSet != null && minSortedSet.size() > 0 && maxSortedSet.size() > 0) {
				if (this.subsymbolicDataRepresentation.size() > perceptDescription.subsymbolicDataRepresentation.size()) {
					minSortedSet = perceptDescription.subsymbolicDataRepresentation;
					maxSortedSet = this.subsymbolicDataRepresentation;
				}
				String key;
				for (Entry<String, Float> entry : minSortedSet.entrySet()) {
					key = entry.getKey();
					if (maxSortedSet.containsKey(key)) {
						subsymbolicValue += entry.getValue() * maxSortedSet.get(key);
					}
				}
			}

			float sum = symbolicValue + subsymbolicValue;
			result = (float) (sum / module);
		}
		return result;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.name).append(' ');
		if (this.symbolicDataRepresentation != null && !this.symbolicDataRepresentation.isEmpty()) {
			for (String s : this.symbolicDataRepresentation) {
				sb.append(s).append(' ');
			}
		}
		if (this.subsymbolicDataRepresentation != null && !this.subsymbolicDataRepresentation.isEmpty()) {
			for (Entry<String, Float> entry : this.subsymbolicDataRepresentation.entrySet()) {
				sb.append(entry.getKey()).append(' ').append(entry.getValue()).append(' ');
			}
		}
		return sb.toString();
	}

}
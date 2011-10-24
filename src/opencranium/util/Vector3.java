package opencranium.util;

/**
 * A class vector to make operations.
 * 
 * @author Jorge Muñoz
 * @author Raúl Arrabales
 */
public class Vector3 {

	/**
	 * X coordinate.
	 */
	private float x;

	/**
	 * Y coordinate.
	 */
	private float y;

	/**
	 * Z coordinate.
	 */
	private float z;

	/**
	 * Default constructor. Creates the vector 0,0,0
	 */
	public Vector3() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}

	/**
	 * 
	 * @param x
	 *            X coordinate.
	 * @param y
	 *            Y coordinate.
	 * @param z
	 *            Z coordinate.
	 */
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Copy constructor
	 * 
	 * @param vector
	 *            The vector to copy.
	 */
	public Vector3(Vector3 vector) {
		if (vector == null) {
			throw new IllegalArgumentException();
		}
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

	/**
	 * Adds to this vector another one and returns this object.
	 * 
	 * @param v
	 *            The other vector.
	 * @return The sum vector, this object.
	 */
	public Vector3 sum(Vector3 v) {
		if (v == null) {
			throw new IllegalArgumentException();
		}
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}

	/**
	 * Subtract to this vector another one and returns this object.
	 * 
	 * @param v
	 *            The other vector.
	 * @return The subtracted vector, this object.
	 */
	public Vector3 subtract(Vector3 v) {
		if (v == null) {
			throw new IllegalArgumentException();
		}
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}

	/**
	 * Make a dot product of this vector with another one.
	 * 
	 * @param v
	 *            The other vector.
	 * @return The dotproduct.
	 */
	public float dotProduct(Vector3 v) {
		if (v == null) {
			throw new IllegalArgumentException();
		}
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}

	/**
	 * Makes a cross product to this vector with another one and returns this
	 * object.
	 * 
	 * @param v
	 *            The other vector.
	 * @return The cross product vector, this object.
	 */
	public Vector3 crossProduct(Vector3 v) {
		if (v == null) {
			throw new IllegalArgumentException();
		}
		float fx = this.y * v.z - this.z * v.y;
		float fy = this.z * v.x - this.x * v.z;
		float fz = this.x * v.y - this.y * v.x;
		this.x = fx;
		this.y = fy;
		this.z = fz;
		return this;
	}

	/**
	 * Normalizes this vector. The module of the vector is 1.
	 * 
	 * @return The normalized vector, this object.
	 */
	public Vector3 normalize() {
		float module = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		this.x /= module;
		this.y /= module;
		this.z /= module;
		return this;
	}

	/**
	 * Multiply this vector by a number.
	 * 
	 * @param n
	 *            The number to multiply this vector.
	 * @return The vector result, this object.
	 */
	public Vector3 multiply(float n) {
		this.x *= n;
		this.y *= n;
		this.z *= n;
		return this;
	}

	/**
	 * Checks that this vector is equals to another one.
	 * 
	 * @param vector
	 *            The other vector.
	 * @return True if both vectors are equals.
	 */
	public boolean equals(Vector3 vector) {
		if (vector == null) {
			throw new IllegalArgumentException();
		}
		return this.x == vector.x && this.y == vector.y && this.z == vector.z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector3) {
			return this.equals((Vector3) obj);
		} else {
			return super.equals(obj);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) (this.x + this.y + this.z);
	}

	/**
	 * X coordinate
	 * 
	 * @return X coordinate
	 */
	public float X() {
		return this.x;
	}

	/**
	 * Y coordinate
	 * 
	 * @return Y coordinate
	 */
	public float Y() {
		return this.y;
	}

	/**
	 * Z coordinate
	 * 
	 * @return Z coordinate
	 */
	public float Z() {
		return this.z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + '(' + this.x + ',' + this.y + ',' + this.z + ')';
	}

}
package fi.breakwaterworks.config;

public enum Permission {
	Read(1), Write(2), Admin(16);

	public final int value;

	private Permission(int value) {
		this.value = value;
	}
}

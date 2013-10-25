
public class Main {
	public static void main(String[] args) {
		new Main();
	}
	Config config;
	Gui gui;
	Compile compile;
	Update update;
	Hero hero;
	
	public Main() {
		config = new Config(this);
		gui = new Gui(this);
		compile = new Compile(this);
		update = new Update(this);
	}
}

package yesman.epicfight.client.events.engine;

import yesman.epicfight.fabric.shim.IEventBus;

public interface IEventBasedEngine {
	public void gameEventBus(IEventBus gameEventBus);
	
	public void modEventBus(IEventBus modEventBus);
	
	public static void init(IEventBus gameEventBus, IEventBus modEventBus) {
		RenderEngine.getInstance().gameEventBus(gameEventBus);
		RenderEngine.getInstance().modEventBus(modEventBus);
		ControlEngine.getInstance().gameEventBus(gameEventBus);
		ControlEngine.getInstance().modEventBus(modEventBus);
	}
}

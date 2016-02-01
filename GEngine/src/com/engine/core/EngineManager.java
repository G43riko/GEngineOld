package com.engine.core;

public abstract class EngineManager extends CoreEngine{
	private GameAble game;
	
	public final void loadGame(GameAble game){
		this.game = game;
	}
	
	@Override
	public void update(float delta) {
		if(game != null)
			game.update(delta);
	}
	
	@Override
	public void input() {
		if(game != null)
			game.input();
	}
}

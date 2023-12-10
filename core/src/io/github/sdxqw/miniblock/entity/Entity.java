package io.github.sdxqw.miniblock.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import io.github.sdxqw.miniblock.world.Box2DHelper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity implements Disposable {
    public static final int ENTITY_SIZE = 1;
    private float x;
    private float y;
    private float speed;
    private World world;
    private Body body2D;

    public Entity(float x, float y, float speed, World world) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.world = world;
        this.body2D = Box2DHelper.createBody(world, ENTITY_SIZE / 2f, ENTITY_SIZE / 2f, new Vector2(x, y), BodyDef.BodyType.KinematicBody);
    }

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch batch);

}

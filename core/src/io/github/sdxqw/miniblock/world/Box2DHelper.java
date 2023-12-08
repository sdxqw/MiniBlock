package io.github.sdxqw.miniblock.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Box2DHelper {
    public static Body createBody(World world, float width, float height, Vector2 pos, BodyDef.BodyType type) {
        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pos.x + width / 2, pos.y + height / 2);
        bodyDef.angle = 0;
        bodyDef.fixedRotation = true;
        bodyDef.type = type;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width, height);

        fixtureDef.shape = boxShape;

        body.createFixture(fixtureDef);
        boxShape.dispose();

        return body;
    }
}

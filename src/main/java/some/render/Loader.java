package some.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Loader {
    private static List<Integer> vertexArrObjs = new ArrayList<>();
    private static List<Integer> vertexBuffObjs = new ArrayList<>();

    private static FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static void storeData(int attribute, int dimensions, float[] data) {
        int vbo = glGenBuffers();
        vertexBuffObjs.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = createFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attribute,
                dimensions,
                GL_FLOAT,
                false,
                0,
                0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private static void bindIndices(int[] data) {
        int vbo = glGenBuffers();
        vertexBuffObjs.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = createIntBuffer(data);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public static Mesh createMesh(float[] positions, int[] indices) {
        int vao = genVao();
        storeData(0, 3, positions);
        bindIndices(indices);
        glBindVertexArray(0);
        return new Mesh(vao,indices.length);
    }

    private static int genVao() {
        int vao = glGenVertexArrays();
        vertexArrObjs.add(vao);
        glBindVertexArray(vao);
        return vao;
    }
}

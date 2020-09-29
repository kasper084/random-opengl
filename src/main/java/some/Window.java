package some;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import some.render.Loader;
import some.render.Mesh;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private static final String TITLE = "window";

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    private long window;

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW not accessible");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);

        if (window == NULL) {
            throw new IllegalStateException("Window was not created");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

    }

    public void loop() {
        float[] vertices = {0.5f, -0.5f, 0f,
                -0.5f, 0.5f, 0f,
                0f, 0.5f, 0f,};
        int[] indices = {0, 1, 2};

        GL.createCapabilities();

        //mix of RGB in float values from 0.0 to 1.0
        glClearColor(0.9f, 0.9f, 0.8f, 0.0f); //lite-grey

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Mesh mesh = Loader.createMesh(vertices, indices);

            glBindVertexArray(mesh.getVaoID());
            glEnableVertexAttribArray(0);
            glDrawElements(GL_TRIANGLES, mesh.getVerticesCount(), GL_UNSIGNED_INT,0);
            glDisableVertexAttribArray(0);
            glBindVertexArray(0);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

}

package net.clydo.clytil;

/**
 * Utility class for mouse-over hitbox detection.
 */
public class Mouses {

    /**
     * Checks if the mouse is over a rectangular area.
     *
     * @param x       The X position of the area.
     * @param y       The Y position of the area.
     * @param width   The width of the area.
     * @param height  The height of the area.
     * @param mouseX  The X position of the mouse.
     * @param mouseY  The Y position of the mouse.
     * @return True if the mouse is over the area.
     */
    public boolean isOver(
            final float x,
            final float y,
            final float width,
            final float height,
            final double mouseX,
            final double mouseY
    ) {
        return isOverInternal(x, y, width, height, mouseX, mouseY, true, true);
    }

    /**
     * Checks if the mouse is over a rectangular area considering active and visible states.
     *
     * @param x       The X position of the area.
     * @param y       The Y position of the area.
     * @param width   The width of the area.
     * @param height  The height of the area.
     * @param mouseX  The X position of the mouse.
     * @param mouseY  The Y position of the mouse.
     * @param active  Whether the area is active.
     * @param visible Whether the area is visible.
     * @return True if the mouse is over the area and it's active and visible.
     */
    public boolean isOver(
            final float x,
            final float y,
            final float width,
            final float height,
            final double mouseX,
            final double mouseY,
            final boolean active,
            final boolean visible
    ) {
        return isOverInternal(x, y, width, height, mouseX, mouseY, active, visible);
    }

    /**
     * Overload for integer coordinates.
     */
    public boolean isOver(
            final int x,
            final int y,
            final int width,
            final int height,
            final double mouseX,
            final double mouseY
    ) {
        return isOver((float) x, (float) y, (float) width, (float) height, mouseX, mouseY);
    }

    /**
     * Overload for integer coordinates with state checks.
     */
    public boolean isOver(
            final int x,
            final int y,
            final int width,
            final int height,
            final double mouseX,
            final double mouseY,
            final boolean active,
            final boolean visible
    ) {
        return isOver((float) x, (float) y, (float) width, (float) height, mouseX, mouseY, active, visible);
    }

    /**
     * Internal method handling the logic.
     */
    private boolean isOverInternal(
            final float x,
            final float y,
            final float width,
            final float height,
            final double mouseX,
            final double mouseY,
            final boolean active,
            final boolean visible
    ) {
        if (!active || !visible || width < 0 || height < 0) return false;
        return mouseX >= x && mouseY >= y && mouseX < (x + width) && mouseY < (y + height);
    }
}

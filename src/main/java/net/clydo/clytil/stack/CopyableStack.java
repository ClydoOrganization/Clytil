/*
 * This file is part of Clytil.
 *
 * Clytil is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Clytil is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Clytil. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2026 ClydoNetwork
 */

package net.clydo.clytil.stack;

import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

/**
 * A stack with an optional free list: popped elements are recycled on the
 * next {@link #push(Copyable)} instead of being discarded, which keeps
 * per-frame transform/render stacks allocation-free.
 *
 * <p>Elements pushed before the first {@link #pop()} define the stack base;
 * the base can never be popped.</p>
 *
 * @param <E> the element type
 */
public class CopyableStack<E extends Copyable<E>> {

    private final Deque<E> freeStack;
    private final Deque<E> stack;
    @Getter
    private final int initialSize;

    /**
     * @param elements the stack base, may be {@code null} for an empty stack
     */
    public CopyableStack(
            @Nullable final E[] elements
    ) {
        this.stack = new ArrayDeque<>();

        if (elements != null) {
            this.initialSize = elements.length;
            Collections.addAll(this.stack, elements);
        } else {
            this.initialSize = 0;
        }

        this.freeStack = this.isSingle() ? null : new ArrayDeque<>();
    }

    /**
     * Pushes a copy of the current top element.
     *
     * @throws java.util.NoSuchElementException if the stack is empty
     */
    public void push() {
        val element = this.stack.getLast();

        this.push(element);
    }

    /**
     * Pushes a copy of {@code element}, reusing a recycled instance when available.
     *
     * @param element the element to push
     */
    public void push(
            @NotNull final E element
    ) {
        var freeElement = element;

        if (this.freeStack != null) {
            freeElement = this.freeStack.pollLast();
            if (freeElement == null) {
                freeElement = element.copy();
            } else {
                freeElement.copyFrom(element);
            }
        }

        this.stack.addLast(freeElement);
    }

    /**
     * Pops the top element, returning it to the free list.
     *
     * @throws IllegalStateException if the stack base would be popped
     */
    public void pop() {
        if (this.stack.size() > this.getInitialSize()) {
            val removedElement = this.stack.removeLast();

            if (removedElement != null && this.freeStack != null) {
                this.freeStack.add(removedElement);
            }
            return;
        }

        throw new IllegalStateException("Cannot pop elements when stack size is less than " + this.getInitialSize());
    }

    /**
     * @return the top element
     * @throws java.util.NoSuchElementException if the stack is empty
     */
    public E lastOrThrow() {
        return this.stack.getLast();
    }

    /**
     * @return the top element, or {@code null} if the stack is empty
     */
    @Nullable
    public E last() {
        return this.stack.peekLast();
    }

    /**
     * @return {@code true} if the stack is back at its base size
     */
    public boolean clear() {
        return (this.stack.size() == this.getInitialSize());
    }

    private boolean isSingle() {
        return this.getInitialSize() == 0;
    }

}

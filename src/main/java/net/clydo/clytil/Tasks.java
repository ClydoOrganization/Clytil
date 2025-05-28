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
 * Copyright (C) 2025 ClydoNetwork
 */

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

/**
 * Utility class for managing sequences of tasks with support for reversible tasks.
 */
@UtilityClass
public class Tasks {

    /**
     * Executes a sequence of tasks and returns the first task that fails.
     *
     * @param tasks the sequence of tasks to execute
     * @return the first failed task, or {@code null} if all tasks succeed
     */
    public @Nullable Task sequenceFailed(Task @NotNull ... tasks) {
        for (val task : tasks) {
            if (!task.execute()) {
                // Uncomment the logger below if a logging framework is set up
                // LOGGER.warn("Task {} failed during execution", task);
                return task; // Return the failed task
            }
        }
        return null;
    }

    /**
     * Executes a sequence of tasks and returns whether all tasks succeeded.
     *
     * @param tasks the sequence of tasks to execute
     * @return {@code true} if all tasks succeed, {@code false} otherwise
     */
    public boolean sequence(Task @NotNull ... tasks) {
        return sequenceFailed(tasks) == null;
    }

    /**
     * Executes a sequence of reversible tasks. If any task fails, it reverts
     * all previously executed tasks in reverse order.
     *
     * @param tasks the sequence of reversible tasks to execute
     * @return {@code true} if all tasks succeed, {@code false} otherwise
     */
    public static boolean sequenceRollback(ReversibleTask @NotNull ... tasks) {
        val executedTasks = new ArrayList<ReversibleTask>();

        for (val task : tasks) {
            if (task.execute()) {
                executedTasks.add(task);
            } else {
                // Uncomment the logger below if a logging framework is set up
                // LOGGER.warn("Task {} failed during execution, starting rollback", task);

                // Revert previously executed tasks in reverse order
                for (int i = executedTasks.size() - 1; i >= 0; i--) {
                    val executedTask = executedTasks.get(i);
                    // Uncomment the logger below if a logging framework is set up
                    // LOGGER.info("Reverting task {}", executedTask);
                    executedTask.revert();
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a reversible task from a given task and its rollback action.
     *
     * @param task     the task to execute
     * @param rollback the task to execute for rollback
     * @return a new {@link ReversibleTask} instance
     */
    public ReversibleTask reversible(Task task, Task rollback) {
        return new ReversibleTask(task, rollback);
    }

    /**
     * Represents a generic task that can be executed.
     */
    @FunctionalInterface
    public interface Task {

        /**
         * Executes the task.
         *
         * @return {@code true} if the task succeeds, {@code false} otherwise
         */
        boolean execute();
    }

    /**
     * Represents a reversible task that can be executed and reverted.
     *
     * @param task     the task to execute
     * @param rollback the task to execute for rollback
     */
    public record ReversibleTask(Task task, Task rollback) implements Task {

        /**
         * Executes the reversible task.
         *
         * @return {@code true} if the task succeeds, {@code false} otherwise
         */
        @Override
        public boolean execute() {
            return this.task.execute();
        }

        /**
         * Reverts the reversible task.
         *
         * @return {@code true} if the rollback succeeds, {@code false} otherwise
         */
        public boolean revert() {
            return this.rollback.execute();
        }
    }
}

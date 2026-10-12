package com.internal.tasktracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void searchAppliesStatusAndArchiveFiltersToTitleAndDescriptionMatches() {
        Task wrongStatusTitle = task("unique-query-token wrong status title", "unrelated", "OPEN", false);
        Task archivedTitle = task("unique-query-token archived title", "unrelated", "DONE", true);
        Task wrongStatusDescription = task("unrelated", "unique-query-token wrong status description", "OPEN", false);
        Task archivedDescription = task("unrelated", "unique-query-token archived description", "DONE", true);
        Task matchingTitle = task("unique-query-token matching title", "unrelated", "DONE", false);
        Task matchingDescription = task("unrelated", "unique-query-token matching description", "DONE", false);

        taskRepository.saveAll(List.of(
                wrongStatusTitle,
                archivedTitle,
                wrongStatusDescription,
                archivedDescription,
                matchingTitle,
                matchingDescription));

        assertThat(taskRepository.searchTasks("%unique-query-token%", "DONE"))
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder(matchingTitle.getTitle(), matchingDescription.getTitle());
    }

    private Task task(String title, String description, String status, boolean archived) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setArchived(archived);
        return task;
    }
}

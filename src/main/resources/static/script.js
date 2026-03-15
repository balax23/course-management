const apiBase = "/api/courses";

document.getElementById("courseForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const course = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        durationInHours: parseInt(document.getElementById("durationInHours").value),
        instructorId: parseInt(document.getElementById("instructorId").value),
        categoryIds: parseIdList(document.getElementById("categoryIds").value),
        studentIds: parseIdList(document.getElementById("studentIds").value)
    };

    const response = await fetch(apiBase, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(course)
    });

    if (response.ok) {
        alert("Course created successfully");
        document.getElementById("courseForm").reset();
        loadCourses();
    } else {
        const error = await response.json();
        alert("Error: " + error.error);
    }
});

function parseIdList(value) {
    if (!value.trim()) {
        return [];
    }

    return value
        .split(",")
        .map(item => parseInt(item.trim()))
        .filter(item => !isNaN(item));
}

async function loadCourses() {
    const response = await fetch(apiBase);
    const courses = await response.json();

    const courseList = document.getElementById("courseList");
    courseList.innerHTML = "";

    courses.forEach(course => {
        const card = document.createElement("div");
        card.className = "course-card";

        card.innerHTML = `
            <h3>${course.title}</h3>
            <p><strong>Description:</strong> ${course.description ?? ""}</p>
            <p><strong>Duration:</strong> ${course.durationInHours} hours</p>
            <p><strong>Instructor:</strong> ${course.instructorName} (${course.instructorEmail})</p>
            <p><strong>Categories:</strong> ${course.categories ? Array.from(course.categories).join(", ") : ""}</p>
            <p><strong>Students:</strong> ${course.students ? Array.from(course.students).join(", ") : ""}</p>
            <button class="delete-btn" onclick="deleteCourse(${course.id})">Delete</button>
        `;

        courseList.appendChild(card);
    });
}

async function deleteCourse(id) {
    const response = await fetch(`${apiBase}/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        alert("Course deleted successfully");
        loadCourses();
    } else {
        alert("Failed to delete course");
    }
}
document.getElementById("enrollForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const courseId = document.getElementById("enrollCourseId").value;
    const studentId = document.getElementById("enrollStudentId").value;

    const response = await fetch(`/api/courses/${courseId}/enroll/${studentId}`, {
        method: "POST"
    });

    if (response.ok) {
        alert("Student enrolled successfully");
        document.getElementById("enrollForm").reset();
        loadCourses();
    } else {
        const error = await response.json();
        alert("Error: " + error.error);
    }
});

loadCourses();
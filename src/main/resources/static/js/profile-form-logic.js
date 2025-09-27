// Function to get the current count of elements to determine the next index
function getNextIndex(containerId, className) {
    const container = document.getElementById(containerId);
    return container.querySelectorAll('.' + className).length;
}

// ----------------------
// 1. WORK EXPERIENCE LOGIC (List Name: experiences)
// ----------------------
function addExperience() {
    const container = document.getElementById('experience-container');
    // NOTE: Use 'experiences' as the class name to count entries
    const index = getNextIndex('experience-container', 'experience-entry'); 

    // HTML Template for a new experience entry
    const newEntryHtml = `
        <div class="experience-entry" id="exp-${index}">
            <hr class="mt-4 mb-4">
            <input type="hidden" name="experiences[${index}].id" value="" /> 
            
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Company *</label>
                    <input type="text" name="experiences[${index}].companyName" class="form-control" required>
                </div>
                <div class="form-group col-md-6">
                    <label>Job Title *</label>
                    <input type="text" name="experiences[${index}].jobTitle" class="form-control" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Start Date</label>
                    <input type="date" name="experiences[${index}].startDate" class="form-control">
                </div>
                <div class="form-group col-md-6">
                    <label>End Date (or leave empty if current)</label>
                    <input type="date" name="experiences[${index}].endDate" class="form-control">
                </div>
            </div>

            <div class="form-group">
                <label>Responsibilities/Achievements</label>
                <textarea name="experiences[${index}].description" class="form-control" rows="3"></textarea>
            </div>
            <button type="button" class="btn btn-sm btn-outline-danger" onclick="removeExperience(this)">
                <i class="fa fa-trash"></i> Remove Experience
            </button>
        </div>
    `;
    
    container.insertAdjacentHTML('beforeend', newEntryHtml);
}

function removeExperience(button) {
    const entry = button.closest('.experience-entry');
    entry.remove();
    // Re-indexing logic is complex and skipped for now, assuming the server will handle missing indices.
}

// ----------------------
// 2. EDUCATION LOGIC (List Name: education)
// ----------------------
function addEducation() {
    const container = document.getElementById('education-container');
    // NOTE: Use 'education' as the class name to count entries
    const index = getNextIndex('education-container', 'education-entry');

    // HTML Template for a new education entry
    const newEntryHtml = `
        <div class="education-entry" id="edu-${index}">
            <hr class="mt-4 mb-4">
            <input type="hidden" name="education[${index}].id" value="" />
            
            <div class="form-group">
                <label>Degree/Certification *</label>
                <input type="text" name="education[${index}].degree" class="form-control" required>
            </div>
            <div class="form-group">
                <label>Institution Name *</label>
                <input type="text" name="education[${index}].schoolName" class="form-control" required>
            </div>
            
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Major</label>
                    <input type="text" name="education[${index}].major" class="form-control">
                </div>
                <div class="form-group col-md-6">
                    <label>Graduation Year</label>
                    <input type="number" name="education[${index}].graduationYear" class="form-control" min="1900" max="2100">
                </div>
            </div>
            
            <div class="form-group">
                <label>GPA</label>
                <input type="number" step="0.01" name="education[${index}].gpa" class="form-control">
            </div>

            <button type="button" class="btn btn-sm btn-outline-danger" onclick="removeEducation(this)">
                <i class="fa fa-trash"></i> Remove Education
            </button>
        </div>
    `;
    
    container.insertAdjacentHTML('beforeend', newEntryHtml);
}

function removeEducation(button) {
    const entry = button.closest('.education-entry');
    entry.remove();
}

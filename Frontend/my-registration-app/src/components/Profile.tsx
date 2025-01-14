import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Style/Profile.css";
import { AppContext } from "../context";
import { ResumeUserData } from "./create-resume/ResumeUserData";

interface ProfileProps {
  onLogout: () => void;
}

const Profile: React.FC<ProfileProps> = ({ onLogout }) => {
  const [profileData, setProfileData] = useState({
    id: "",
    username: "",
    email: "",
    phone: "",
    gender: "",
    age: "",
    password: "",
  });

  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState(profileData);

  const context = useContext(AppContext);

  const [resumes, setResumes] = useState<ResumeUserData[]>([]);
  const [resumeAvailable, setResumeAvailable] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    const userId = context?.userId;
    console.log("Session ID from context:", userId);
    if (!userId) {
      console.error("User ID not found in context");
      navigate("/login");
      return;
    }

    // Fetch profile data
    axios
      .get(`http://localhost:8080/users/one/${userId}`, { withCredentials: true })
      .then(async (response) => {
        setProfileData(response.data);
        setFormData(response.data);
      })
      .catch((error) => console.error("Error fetching profile data:", error));

    const fetchResumes = async () => {
      try {
        const response = await fetch(`http://localhost:8080/resumes/userId/${userId}`);
        const data = await response.json();
        setResumes(data.resumes); // Встановлюємо масив резюме в state
        setResumeAvailable(data.resumes.length > 0); // Встановлюємо статус доступності
      } catch (error) {
        console.error('Error fetching resumes:', error);
        setResumeAvailable(false);
      }
    };

    fetchResumes();
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSave = () => {
    axios
      .put(`http://localhost:8080/users`, formData, {
        withCredentials: true,
      })
      .then((response) => {
        setProfileData(response.data);
        setIsEditing(false);
      })
      .catch((error) => console.error("Error updating profile:", error));
  };

  const handleViewResume = (resume: ResumeUserData, template: number) => {
    navigate(`/template${template}`, { state: { data: resume } });
  };

  return (
    <div>
      <div className="profilePanel">
        <nav className="navbar-profile">
          <a className="username">{profileData.username || "Username"}</a>
          <button className="btn-logout" onClick={onLogout}>
            Logout
          </button>
        </nav>
      </div>
      <div className="wrapper">
        <div className="profile-container">
          <div className="navbar-profile">
            <h2>My profile</h2>
            {!isEditing ? (
              <a className="icon-edit" onClick={() => setIsEditing(true)}>
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  height="24px"
                  viewBox="0 -960 960 960"
                  width="24px"
                  fill="#D9D9D9"
                >
                  <path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z" />
                </svg>
              </a>
            ) : (
              <></>
            )}
          </div>
          {!isEditing ? (
            <div className="infoFields">
              <p>Name: {profileData.username}</p>
              <p>Email: {profileData.email}</p>
              <p>Phone: {profileData.phone}</p>
              <p>Gender: {profileData.gender}</p>
              <p>Age: {profileData.age}</p>
            </div>
          ) : (
            <div className="infoFields">
              <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleInputChange}
                placeholder="Name"
              />
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                placeholder="Email"
              />
              <input
                type="text"
                name="phone"
                value={formData.phone}
                onChange={handleInputChange}
                placeholder="Phone"
              />
              <select
                id="gender"
                name="gender"
                value={formData.gender}
                onChange={handleInputChange}
                required
              >
                <option value="" disabled>
                  Select your gender
                </option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
              <input
                type="number"
                name="age"
                value={formData.age}
                onChange={handleInputChange}
                placeholder="Age"
              />
              <div className="buttons-box">
                <button className="btn-save" onClick={handleSave}>
                  Save
                </button>
                <button
                  className="btn-cancel"
                  onClick={() => setIsEditing(false)}
                >
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
      <div className="resume-profile-container">
        <h3>My resumes</h3>
        
        {resumes && resumes.length > 0 ? (
          <ul className="resume-list">
            {resumes.map((resume) => (
              <li key={resume.id} className="resume-item">
                <span>{resume.position}</span>
                <div>
                  <button className="btn-edit">Edit Resume</button>
                  <button className="btn-view"
                    onClick={() => handleViewResume(resume, resume.template)}>View Resume</button>
                </div>
              </li>
            ))}
          </ul>
        ) : (
          <div className="create-resume">
            <p className="text">You haven't created any resumes yet.</p>
            <button className="btn-add" onClick={() => navigate("/create-resume")}>
              Create now
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Profile;

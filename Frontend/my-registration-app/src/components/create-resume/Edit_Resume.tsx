import React, { useState, useEffect, useContext } from 'react';
import "../Style/ResumeStyle.css";
import { ResumeData } from './ResumeInterface';
import { AppContext } from "../../context";
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

const EditResume: React.FC = () => {
  const [formData, setFormData] = useState<ResumeData>({
    fullName: '',
    position: '',
    objective: '',
    education: '',
    workExperience: '',
    skillsAndAwards: '',
    languages: '',
    recommendations: '',
    hobbiesAndInterests: ''
  });

  const [template, setTemplate] = useState<number>(1);
  const { id } = useParams();
  const navigate = useNavigate();
  const context = useContext(AppContext);
  const userId = context?.userId;

  // Получение данных резюме для редактирования
  useEffect(() => {
    if (id) {
      axios
        .get(`http://localhost:8080/resumes/${id}`)
        .then(response => {
          const { template, ...rest } = response.data;
          setTemplate(template || 1);
          setFormData(rest);
        })
        .catch(error => {
          console.error("Error fetching resume data:", error);
        });
    }
  }, [id]);

  // Обработка изменений полей формы
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  // Выбор темлейта
  const handleTemplateChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setTemplate(Number(e.target.value));
  };

  // Обработка отправки формы для редактирования резюме
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const payload = { ...formData, userId, template };

    try {
      const response = await axios.put(`http://localhost:8080/resumes/${id}`, payload, {
        headers: { 'Content-Type': 'application/json' }
      });
      console.log('Resume updated successfully:', response.data);
      alert('Resume updated successfully!');
      navigate('/profile');
    } catch (error) {
      console.error('Error updating resume:', error);
      alert('Failed to update the resume.');
    }
  };

  return (
    <form className="resume-form" onSubmit={handleSubmit}>
      <h2 className="title">Edit Resume</h2>
      <div className="form-group">
        <label>Choose the template:</label>
        <select value={template} onChange={handleTemplateChange}>
          <option value={1}>Template 1</option>
          <option value={2}>Template 2</option>
          <option value={3}>Template 3</option>
        </select>
      </div>
      <div className="form-group">
        <label>Full name:</label>
        <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Position:</label>
        <input type="text" name="position" value={formData.position} onChange={handleChange} />
      </div>
      <div className="form-group">
        <label>Aim:</label>
        <textarea name="objective" value={formData.objective} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Education:</label>
        <textarea name="education" value={formData.education} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Working experience:</label>
        <textarea name="workExperience" value={formData.workExperience} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Skills and awards:</label>
        <textarea name="skillsAndAwards" value={formData.skillsAndAwards} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Languages:</label>
        <textarea name="languages" value={formData.languages} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Recommendations:</label>
        <textarea name="recommendations" value={formData.recommendations} onChange={handleChange}></textarea>
      </div>
      <div className="form-group">
        <label>Hobbies and interests:</label>
        <textarea name="hobbiesAndInterests" value={formData.hobbiesAndInterests} onChange={handleChange}></textarea>
      </div>
      <button type="submit" className="btn-save-resume">Save Changes</button>
    </form>
  );
};

export default EditResume;

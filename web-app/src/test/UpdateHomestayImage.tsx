import React, { useState, ChangeEvent, FormEvent } from 'react';

interface UpdateHomestayImageRequest {
    content: string;
}

interface UpdateHomestayImageProps {
    homestayId: number;
}

const UpdateHomestayImage: React.FC<UpdateHomestayImageProps> = ({ homestayId }) => {
    const [primaryFile, setPrimaryFile] = useState<File | null>(null);
    const [relatedFiles, setRelatedFiles] = useState<File[]>([]);
    const [formData, setFormData] = useState<UpdateHomestayImageRequest>({
        content: '',
    });
    const [message, setMessage] = useState<string | null>(null);

    const handlePrimaryFileChange = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            setPrimaryFile(event.target.files[0]);
        }
    };

    const handleRelatedFilesChange = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            setRelatedFiles(Array.from(event.target.files));
        }
    };

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setFormData((prevFormData) => ({
            ...prevFormData,
            [name]: value,
        }));
    };

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();

        const form = new FormData();

        if (primaryFile) {
            form.append('primary', primaryFile); // Gửi tệp chính với key 'primary'
        }

        relatedFiles.forEach((file) => form.append('related', file)); // Gửi tệp liên quan với key 'related'

        form.append(
            'request',
            new Blob([JSON.stringify(formData)], { type: 'application/json' }) // Gửi JSON với key 'request'
        );

        try {
            const response = await fetch(
                `http://localhost:8080/api/v1/homestays/${homestayId}/images`,
                {
                    method: 'PUT',
                    body: form, // Không cần Content-Type vì FormData tự động thiết lập
                }
            );

            if (response.ok) {
                setMessage('Cập nhật hình ảnh homestay thành công');
            } else {
                setMessage('Đã xảy ra lỗi khi cập nhật hình ảnh');
            }
        } catch (error) {
            setMessage('Lỗi kết nối với server');
            console.error(error);
        }
    };

    return (
        <div>
            <h2>Cập nhật hình ảnh Homestay</h2>
            {message && <p>{message}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Ảnh chính:</label>
                    <input
                        type="file"
                        onChange={handlePrimaryFileChange}
                        accept="image/*"
                    />
                </div>
                <div>
                    <label>Ảnh/video liên quan:</label>
                    <input
                        type="file"
                        multiple
                        onChange={handleRelatedFilesChange}
                        accept="image/*,video/*" // Giới hạn kiểu tệp là ảnh hoặc video
                    />
                </div>
                <div>
                    <label>Content:</label>
                    <input
                        type="text"
                        name="content"
                        value={formData.content}
                        onChange={handleInputChange}
                    />
                </div>
                <button type="submit">Cập nhật</button>
            </form>
        </div>
    );
};

export default UpdateHomestayImage;

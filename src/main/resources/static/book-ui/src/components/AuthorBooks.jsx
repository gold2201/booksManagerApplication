import React, { useCallback, useEffect, useState } from "react";
import { Table, Button, Modal, Form, Input, message, Space, Upload } from "antd";
import axios from "axios";
import { useParams } from "react-router-dom";
import { UploadOutlined } from "@ant-design/icons";

const AuthorBooks = () => {
    const { id } = useParams();
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isModalVisible, setModalVisible] = useState(false);
    const [editBook, setEditBook] = useState(null);
    const [authorName, setAuthorName] = useState("");
    const [form] = Form.useForm();

    const fetchAuthorName = useCallback(async () => {
        try {
            const { data } = await axios.get(`http://localhost:8080/api/v1/authors/${id}`);
            setAuthorName(data.name);
        } catch (e) {
            console.error(e);
            message.error("Не удалось загрузить данные автора");
        }
    }, [id]);

    const fetchBooks = useCallback(async (name) => {
        if (!name) return;
        try {
            setLoading(true);
            const { data } = await axios.get(`http://localhost:8080/api/v1/books/by-author/${name}`);
            setBooks(data);
        } catch (e) {
            console.error(e);
            message.error("Не удалось загрузить книги автора");
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        void fetchAuthorName();
    }, [fetchAuthorName]);

    useEffect(() => {
        if (authorName) {
            void fetchBooks(authorName);
        }
    }, [authorName, fetchBooks]);

    useEffect(() => {
        if (editBook && isModalVisible) {
            const sortedChapters = [...editBook.chapters]
                .filter(c => c.id != null)
                .sort((a, b) => a.id - b.id)
                .map(c => ({ title: c.title, content: c.content }));

            form.setFieldsValue({
                title: editBook.title,
                authors: editBook.authors.map(a => a.name),
                chapters: sortedChapters,
                imageName: editBook.imageName || ""
            });
        }
    }, [editBook, isModalVisible, form]);

    const openAddModal = () => {
        setEditBook(null);
        form.setFieldsValue({
            title: "",
            authors: authorName ? [authorName] : [],
            chapters: [{ title: "", content: "" }],
            imageName: ""
        });
        setModalVisible(true);
    };

    const openEditModal = (book) => {
        setEditBook(book);
        setModalVisible(true);
    };

    const confirmDelete = (bookId) => {
        Modal.confirm({
            title: "Вы уверены, что хотите удалить книгу?",
            okText: "Да, удалить",
            okType: "danger",
            cancelText: "Отмена",
            onOk: async () => {
                try {
                    await axios.delete(`http://localhost:8080/api/v1/books/${bookId}`);
                    message.success("Книга удалена");
                    await fetchBooks(authorName);
                } catch (e) {
                    console.error(e);
                    message.error("Не удалось удалить книгу");
                }
            },
        });
    };


    const handleOk = async () => {
        try {
            const values = await form.validateFields();
            const bookPayload = {
                title: values.title,
                authors: values.authors.map((name) => ({ name })),
                chapters: values.chapters.map((chapter) => ({
                    title: chapter.title,
                    content: chapter.content
                }))
            };

            if (!editBook) {
                bookPayload.imageName = values.imageName;
            }

            if (editBook) {
                await axios.put(`http://localhost:8080/api/v1/books/updateBook/${editBook.id}`, bookPayload);
                message.success("Книга обновлена");
            } else {
                await axios.post(`http://localhost:8080/api/v1/books/create`, bookPayload);
                message.success("Книга создана");
            }

            setModalVisible(false);
            await fetchBooks(authorName);
        } catch (e) {
            console.error(e);
            message.error("Пожалуйста, добавьте изображение");
        }
    };

    const columns = [
        { title: "ID", dataIndex: "id", key: "id", width: 60 },
        { title: "Название", dataIndex: "title", key: "title" },
        {
            title: "Главы",
            dataIndex: "chapters",
            key: "chapters",
            render: (chapters = []) => (
                <ul style={{ paddingLeft: 16 }}>
                    {[...chapters]
                        .filter(c => c.id != null) // фильтруем на всякий случай
                        .sort((a, b) => a.id - b.id)
                        .map((c) => (
                            <li key={c.id}>{c.title}</li>
                        ))}
                </ul>
            )
        },
        {
            title: "Действия",
            key: "actions",
            width: 200,
            render: (_, book) => (
                <Space>
                    <Button size="small" onClick={() => openEditModal(book)}>
                        Редактировать
                    </Button>
                    <Button size="small" danger onClick={() => confirmDelete(book.id)}>
                        Удалить
                    </Button>
                </Space>
            ),
        }
    ];

    return (
        <div style={{ padding: 20 }}>
            <h2>Книги автора: {authorName}</h2>
            <Button type="primary" onClick={openAddModal} style={{ marginBottom: 16 }}>
                Добавить книгу
            </Button>

            <Table
                loading={loading}
                dataSource={books}
                columns={columns}
                rowKey="id"
                pagination={false}
            />

            <Modal
                title={editBook ? "Редактировать книгу" : "Создать новую книгу"}
                open={isModalVisible}
                onOk={handleOk}
                onCancel={() => setModalVisible(false)}
                destroyOnClose
                width={600}
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="title"
                        label="Название книги"
                        rules={[{ required: true, message: "Введите название книги" }]}
                    >
                        <Input />
                    </Form.Item>

                    <Form.List name="authors">
                        {(fields, { add, remove }) => (
                            <div>
                                <label>Авторы</label>
                                {fields.map(field => (
                                    <Space key={field.key} style={{ display: "flex", marginBottom: 8 }} align="baseline">
                                        <Form.Item
                                            name={[field.name]}
                                            rules={[{ required: true, message: "Имя автора" }]}
                                        >
                                            <Input placeholder="Имя автора" />
                                        </Form.Item>
                                        <Button danger onClick={() => remove(field.name)}>-</Button>
                                    </Space>
                                ))}
                                <Button type="dashed" onClick={() => add()} block>
                                    Добавить автора
                                </Button>
                            </div>
                        )}
                    </Form.List>

                    <Form.List name="chapters">
                        {(fields, { add, remove }) => (
                            <div style={{ marginTop: 20 }}>
                                <label>Главы</label>
                                {fields.map(field => (
                                    <Space key={field.key} style={{ display: "flex", marginBottom: 8 }} align="baseline">
                                        <Form.Item
                                            name={[field.name, "title"]}
                                            rules={[{ required: true, message: "Введите название главы" }]}
                                        >
                                            <Input placeholder="Название главы" />
                                        </Form.Item>
                                        <Form.Item
                                            name={[field.name, "content"]}
                                            rules={[{ required: true, message: "Введите содержание главы" }]}
                                        >
                                            <Input placeholder="Содержание главы" />
                                        </Form.Item>
                                        <Button danger onClick={() => remove(field.name)}>-</Button>
                                    </Space>
                                ))}
                                <Button type="dashed" onClick={() => add()} block>
                                    Добавить главу
                                </Button>
                            </div>
                        )}
                    </Form.List>

                    <Form.Item label="Изображение (название файла)">
                        <Upload
                            beforeUpload={(file) => {
                                form.setFieldsValue({ imageName: file.name });
                                return false;
                            }}
                            showUploadList={{ showRemoveIcon: false }}
                        >
                            <Button icon={<UploadOutlined />}>Выбрать изображение</Button>
                        </Upload>
                    </Form.Item>

                    <Form.Item
                        name="imageName"
                        style={{ display: 'none' }}
                        rules={[{ required: !editBook, message: "Выберите изображение" }]}
                    >
                        <Input />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default AuthorBooks;









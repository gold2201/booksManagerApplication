import React, { useState } from "react";
import { Button, Card, Form, Input, message } from "antd";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const AuthorLogin = () => {
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const onFinish = async (values) => {
        setLoading(true);
        try {
            const { data: author } = await axios.post(
                "http://localhost:8080/api/v1/authors/register",
                { name: values.username, password: values.password }
            );
            message.success(`Вход`);
            // Переходим на страницу книг автора:
            navigate(`/author/${author.id}/books`);
        } catch (err) {
            console.error(err);
            message.error(
                err.response?.data?.error || "Ошибка при регистрации, попробуйте другое имя"
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex justify-center items-center min-h-screen bg-gray-100 p-4">
            <Card
                className="shadow-2xl rounded-2xl p-8"
                style={{ width: 380, background: "white" }}
            >
                <h2 className="text-2xl font-semibold mb-6 text-center">
                    Регистрация автора
                </h2>
                <Form name="author_register" onFinish={onFinish} layout="vertical" requiredMark={false}>
                    <Form.Item
                        label="Имя автора"
                        name="username"
                        rules={[{ required: true, message: "Введите ваше имя!" }]}
                    >
                        <Input placeholder="Ваше уникальное имя" />
                    </Form.Item>
                    <Form.Item
                        label="Пароль"
                        name="password"
                        rules={[{ required: true, message: "Придумайте пароль!" }]}
                    >
                        <Input.Password placeholder="Минимум 6 символов" />
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="submit" loading={loading} block>
                            Зарегистрироваться
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default AuthorLogin;


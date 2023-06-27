import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import {Link, useNavigate} from 'react-router-dom';
import {useState} from "react";

const TaskCreate = () => {
    const initialFormState = {
        name: '',
        description: ''
    };
    const [task, setTask] = useState(initialFormState);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    let isCreated = false;

    const handleChange = (event) => {
        const {name, value} = event.target;
        setTask(({...task, [name]: value}));
    };

    const submit = async (event) => {
        event.preventDefault();

        await fetch('/api/tasks', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(task)
        })
            .then((response) => {
                if (response.status === 201) {
                    isCreated = true;
                } else {
                    return response.json();
                }
            })
            .then((data) => {
                if (isCreated) {
                    setTask(initialFormState);
                    navigate('/');
                } else {
                    setError(data.message);
                }
            });
    };

    return (
        <div>
            <Container style={{marginTop: "3%"}}>
                <Form onSubmit={submit}>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" onChange={handleChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="description">Description</Label>
                        <Input type="textarea" name="description" id="description" onChange={handleChange}/>
                        { error ? <span style={{ color: 'red', fontSize: '12px'}}>{error}</span> : '' }
                    </FormGroup>
                    <FormGroup>
                        <div align="center" style={{marginBottom: "10px"}}>
                            <ButtonGroup>
                                <Button color="success" type="submit">Add</Button>
                                <Button color="secondary" tag={Link} to="/">Back</Button>
                            </ButtonGroup>
                        </div>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    );
};

export default TaskCreate;

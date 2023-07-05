import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import {Link, useNavigate} from 'react-router-dom';
import {useState} from "react";
import Select from "react-select";

const TaskCreate = () => {
    const initialFormState = {
        name: '',
        description: '',
        epictaskId: ''
    };
    const taskTypes = [
        { value: "Task", label: "Task" },
        { value: "Epictask", label: "Epictask" },
        { value: "Subtask", label: "Subtask" }
    ];
    const [submitPath, setSubmitPath] = useState('/api/tasks');
    const [type, setType] = useState('Task');
    const [task, setTask] = useState(initialFormState);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    let isCreated = false;
    const [epictasks, setEpictasks] = useState([]);

    const handleChange = (event) => {
        const {name, value} = event.target;
        setTask(({...task, [name]: value}));
    };

    const handleTypeChange = (event) => {
        setType(event.value);
        if (event.value === taskTypes[0].value) {
            setSubmitPath('/api/tasks');
        }
        if (event.value === taskTypes[1].value) {
            setSubmitPath('/api/epictasks');
        }
        if (event.value === taskTypes[2].value) {
            setSubmitPath('/api/subtasks');
            fetch('/api/epictasks', {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            })
            .then(response => response.json())
            .then(data => {setEpictasks(data);});
        }
    };

    const handleEpictaskChange = (event: string[]) => {
        setTask(({...task, ["epictaskId"]: event.id}));
    };

    const submit = async (event) => {
        event.preventDefault();

        await fetch(submitPath, {
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
                        <Label for="type">Type</Label>
                        <Select name="type" id="type" options={taskTypes}
                                defaultValue={taskTypes[0]} onChange={handleTypeChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" onChange={handleChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="description">Description</Label>
                        <Input type="textarea" name="description" id="description" onChange={handleChange}/>
                    </FormGroup>
                    {
                        type === taskTypes[2].value ?
                            <FormGroup>
                                <Label for="epictaskId">Epictask name</Label>
                                <Select name="epictaskId" id="epictaskId" options={epictasks}
                                        getOptionValue={(option) => option.id} getOptionLabel={(option => option.name)}
                                        onChange={handleEpictaskChange}/>
                            </FormGroup>
                        : ''
                    }
                    { error ? <span style={{ color: 'red', fontSize: '12px'}}>{error}</span> : '' }
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

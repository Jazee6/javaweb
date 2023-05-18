<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {message, UploadChangeParam} from "ant-design-vue";
import router from "../main";
import {useStore} from "../store";

const columns = [
    {
        title: 'ID',
        dataIndex: 'id',
    },
    {
        title: 'Name',
        dataIndex: 'name',
    },
    {
        title: 'Hours',
        dataIndex: 'hours',
    },
    {
        title: 'Academy',
        dataIndex: 'schoolName',
        sorter: (a: { school_id: number; }, b: { school_id: number; }) => a.school_id > b.school_id,
    },
    {
        title: 'Image',
        dataIndex: 'img',
        slots: {customRender: 'img'},
    },
    {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'},
    },
]

const data = ref()

onMounted(() => {
    load()
})

const load = () => {
    fetch("http://localhost:5173/api/courses")
        .then(res => res.json())
        .then(res => {
            res.forEach((item: { img: string; }) => {
                if (item.img === null) {
                    item.img = "http://localhost:8080/uploads/undraw_Conference_speaker_re_1rna.png"
                }
            })
            fetch("http://localhost:5173/api/schools")
                .then(res => res.json())
                .then(res2 => {
                    res.forEach((item: { school_id: number; schoolName: string; }) => {
                        res2.forEach((item2: { id: number; schoolName: string; }) => {
                            if (item.school_id === item2.id) {
                                item.schoolName = item2.schoolName
                            }
                        })
                    })
                    data.value = res
                })
        }).catch(err => {
        message.error("未登录")
        setTimeout(() => {
            router.push("/login")
        }, 500)
    })
}
const insertClass = () => {
    visible.value = true
    fetch("http://localhost:5173/api/schools").then(res => res.json()).then(res => {
        console.log(res)
        option.value = res
    })
}

const visible = ref(false)

const addForm = ref(
    {
        name: "",
        hours: "",
        school_id: "",
        img: ""
    }
)

const option = ref()

const submit = () => {
    visible.value = false
    fetch("http://localhost:5173/api/courses", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(addForm.value)
    }).then(res => {
        console.log(res)
    })
    setTimeout(() => {
        load()
    }, 1000)
}

const visible2 = ref(false)

const editForm = ref(
    {
        name: "",
        hours: "",
        school_id: "",
        img: ""
    }
)

const submit2 = () => {
    visible2.value = false
    fetch("http://localhost:5173/api/courses", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(editForm.value)
    }).then(res => res.text()).then(res => {
        if (res !== "success") {
            message.error(res)
        }
    })
    setTimeout(() => {
        load()
        message.success("修改成功！")
    }, 1000)
}

const aaa = (record: any) => {
    visible2.value = true;
    editForm.value = record;
    fetch("http://localhost:5173/api/schools").then(res => res.json()).then(res => {
        console.log(res)
        option.value = res
    })
}

const deletee = (record: any) => {
    visible3.value = true
    r.value = record
}
const r = ref()
const visible3 = ref(false)

const handleok = () => {
    visible3.value = false
    fetch("http://localhost:5173/api/courses/" + r.value.id, {
        method: "DELETE",
    }).then(res => res.text()).then(res => {
        if (res !== "success") {
            message.error(res)
        } else {
            message.success("删除成功")
            load()
        }
    })
}

const cancel = () => {
    visible3.value = false
}

const fileList = ref([])

const handleChange = (info: UploadChangeParam) => {
    if (info.file.status === 'uploading') {
        loading.value = true;
        return;
    }
    if (info.file.status === 'done') {
        getBase64(info.file.originFileObj, (base64Url: string) => {
            imageUrl.value = base64Url;
            loading.value = false;
        });
        addForm.value.img = info.file.response
        editForm.value.img = info.file.response
        message.success('upload successfully');
    }
    if (info.file.status === 'error') {
        loading.value = false;
        message.error('upload error');
    }
};

const loading = ref(false)
const imageUrl = ref()

function getBase64(img: any, callback: (base64Url: string) => void) {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result as string));
    reader.readAsDataURL(img);
}

const store = useStore()

const user = computed(() => {
    return store.username
})
</script>

<template>
    <div style="display: flex;justify-content: space-between">
        <h1 style="margin: 1rem ">课程列表</h1>
        <h4>用户:{{user}} <a-button>登出</a-button> </h4>
    </div>
    <a-divider/>
    <a-modal :visible="visible3" title="确认框" @ok="handleok" @cancel="cancel">确认删除吗？？？？</a-modal>
    <a-button @click="insertClass" style="margin: 1rem">添加课程</a-button>
    <a-table :columns="columns" :data-source="data">
        <template #action="{text, record}">
            <a-button @click="aaa(record)">编辑</a-button>
            <a-button @click="deletee(record)">删除</a-button>
        </template>
        <template #img="{text, record}">
            <a-image :src="record.img" style="max-width: 96px"/>
        </template>
    </a-table>
    <a-drawer title="新增课程" :visible="visible" width="720" @close="visible=false">
        <a-form :model="addForm">
            <a-form-item label="课程名称">
                <a-input v-model:value="addForm.name"/>
            </a-form-item>
            <a-form-item label="课程学时">
                <a-input-number v-model:value="addForm.hours" style="width: 100%"/>
            </a-form-item>
            <a-form-item label="所属学院">
                <a-select v-model:value="addForm.school_id" :options="option"
                          :field-names="{ label: 'schoolName', value: 'id'}">
                </a-select>
            </a-form-item>
            <a-form-item label="课程图片">
                <a-upload
                        v-model:file-list="fileList"
                        name="file"
                        list-type="picture-card"
                        class="avatar-uploader"
                        :show-upload-list="false"
                        action="http://localhost:5173/api/upload"
                        @change="handleChange"
                >
                    <img v-if="imageUrl" :src="imageUrl" alt="avatar" style="width: 128px;height: 128px;"/>
                    <div v-else>
                        <loading-outlined v-if="loading"></loading-outlined>
                        <plus-outlined v-else></plus-outlined>
                        <div class="ant-upload-text">Upload</div>
                    </div>
                </a-upload>
            </a-form-item>
            <a-form-item>
                <a-button type="primary" @click="submit">提交</a-button>
            </a-form-item>
        </a-form>
    </a-drawer>
    <a-drawer title="修改课程" :visible="visible2" width="720" @close="visible2=false">
        <a-form :model="editForm">
            <a-form-item label="课程名称">
                <a-input v-model:value="editForm.name"/>
            </a-form-item>
            <a-form-item label="课程学时">
                <a-input-number v-model:value="editForm.hours" style="width: 100%"/>
            </a-form-item>
            <a-form-item label="所属学院">
                <a-select v-model:value="editForm.school_id" :options="option"
                          :field-names="{ label: 'schoolName', value: 'id'}">
                </a-select>
            </a-form-item>
            <a-form-item label="课程图片">
                <a-upload
                        v-model:file-list="fileList"
                        name="file"
                        list-type="picture-card"
                        class="avatar-uploader"
                        :show-upload-list="false"
                        action="http://localhost:5173/api/upload"
                        @change="handleChange"
                >
                    <img v-if="imageUrl" :src="imageUrl" alt="avatar" style="width: 128px;height: 128px;"/>
                    <div v-else>
                        <loading-outlined v-if="loading"></loading-outlined>
                        <plus-outlined v-else></plus-outlined>
                        <div class="ant-upload-text">Upload</div>
                    </div>
                </a-upload>
            </a-form-item>
            <a-form-item>
                <a-button type="primary" @click="submit2">提交</a-button>
            </a-form-item>
        </a-form>
    </a-drawer>
</template>

<style scoped>
.avatar-uploader {
    width: 128px;
    height: 128px;
}

.ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
}

.ant-upload-select-picture-card {
    margin-top: 8px;
    color: #666;
}

.ant-upload-text {
    margin-top: 8px;
    color: #666;
}
</style>

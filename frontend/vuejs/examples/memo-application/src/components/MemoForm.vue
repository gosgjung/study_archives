<template>
    <div class="memo-form">
        <form @submit.prevent="addMemo">
            <fieldset>
                <div>
                    <!-- v-model을 통해 data 속성의 값을 바인딩한다. -->
                    <input  class="momo-form__title-form"
                            type="text"
                            v-model="title" 
                            placeholder="메모 제목을 입력해주세요."/>
                    <textarea   class="memo-form__content-form"
                                v-model="content"
                                placeholder="메모의 내용을 입력해주세요."/>
                    <button type="reset">
                        <i class="fas fa-sync-alt">
                        </i>
                    </button>
                </div>
                <button type="submit">등록하기</button>
            </fieldset>
        </form>
    </div>
</template>

<script>
export default {
    // 컴포넌트의 이름을 MemoForm으로 변경한다. 
    name: "MemoForm",
    data(){
        return {
            // 사용자가 입력한 데이터(content, title)에 대한 key, value
            // 여기서 등록하는 데이터는 v-model 디렉티브를 이용해 입력폼의 입력필드에 연결해줘야 한다.
            title: '',
            content: '',
        }
    },
    methods: {
        resetFields(){
            // 제목,내용을 빈 값으로 초기화한다. 
            this.title = '';
            this.content = '';
        },
        addMemo(){
            // 변수 선언 (비구조화 할당)
            const {title, content} = this;
            // 데이터의 고유한 식별자를 생성
            const id = new Date().getTime();

            // 제목, 내용을 입력하지 않은 경우에 대한 예외 처리 
            const isEmpty = title.length <=0 || content.length <=0;

            if(isEmpty){
                alert("메모 내용을 입력해주세요");
                return false;
            }

            // addMemo 이벤트를 발생시킨다. 
            // payload에 사용자가 입력한 데이터를 넣어준다. {id, titile, content}
            this.$emit('addMemo', {id, title, content});

            // MemoApp 으로 이벤트와 데이터를 전파한 후 폼 텍스트 초기화
            this.resetFields();
        },
    }
}
</script>
<style scoped>
  .memo-form {
    margin-bottom: 24px;
    padding-bottom: 40px;
    border-bottom: 1px solid #eee;
  }
  .memo-form form fieldset div {
    position: relative;
    padding: 24px;
    margin-bottom: 20px;
    box-shadow: 0 4px 10px -4px rgba(0, 0, 0, 0.2);
    background-color: #ffffff;
  }
  .memo-form form fieldset div button[type="reset"] {
    position: absolute;
    right: 20px;
    bottom: 20px;
    font-size: 16px;
    background: none;
  }
  .memo-form form fieldset button[type="submit"] {
    float: right;
    width: 96px;
    padding: 12px 0;
    border-radius: 4px;
    background-color: #ff5a00;
    color: #fff;
    font-size: 16px;
  }
  .memo-form form fieldset .memo-form__title-form {
    width: 100%;
    margin-bottom: 12px;
    font-size: 18px;
    line-height: 26px;
  }
  .memo-form form fieldset .memo-form__content-form {
    width: 100%;
    height: 66px;
    font-size: 14px;
    line-height: 22px;
    vertical-align: top;
  }
  .memo-form input:focus {
    outline: none;
  }
</style>
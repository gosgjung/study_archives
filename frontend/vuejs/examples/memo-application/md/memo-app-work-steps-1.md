# 작업과정 정리...(메모 애플리케이션)

# 1. reset.css 추가
reset.css는 초기화 CSS 코드이다. 초기화 CSS는 브라우저가 DOM에 기본 적용하는 스타일에 우리의 CSS를 덮어씌워주는 역할을 한다.  
초기화 CSS 코드를 작성하는 이유는 브라우저마다 DOM에 기본으로 적용하는 스타일이 조금씩 다르기 때문이다. 이런 이유로 같은 DOM과 스타일을 작성했더라도 최종적인 렌더링 결과 또한 조금씩 다를 수 있다. 사용자마다 어떤 브라우저에서 페이지를 보게 되더라도 일관도니 UI/UX를 제공해주기 위해서 reset.css를 작성한다.  
폰트 어썸은 벡터형 아이콘과 로고들을 폰트처럼 사용할 수 있도록 해준다.([참고](https://fontawesome.com))  

- src 밑에 styles 디렉터리 생성
    - src/styles/reset.css 생성
    - src/styles/reset.css 코딩
- App.vue 내에 <style> 영역의 코드들을 삭제 후 src/styles/reset.css import

# 2. App Header 추가 (헤더 컴포넌트 생성)
- src 밑에 components 디렉터리 생성
    - src/components/AppHeader.vue 생성
    - src/components/AppHeader.vue 코딩
- App.vue 내에 <script>...</script> 내용들을 삭제후 아래 내용을 코딩
    ```javascript
    <script>
    // AppHeader.vue 임포트
    import AppHeader from './components/AppHeader';

    export default {
        name: 'app',
        components:{
            AppHeader
        }
    }
    </script>
    ```
- App.vue 내에 <template>...</template> 내의 내용들을 삭제후 아래 내용을 코딩
    ```javascript
    <template>
        <div id="app">
            <app-header/>
        </div>
    </template>
    ```

# 3. 메모 생성 기본 컴포넌트 추가
여기서 만들 컴포넌트는 MemoApp.vue, MemoForm.vue 이다.
추후 vuex를 적용시 Memo.vue를 만들 예정이다. 여기서는 MemoApp.vue, MemoForm.vue를 추가한다.  

## MemoApp.vue
현재 작성되고 있는 우리의 애플리케이션에서는 별도의 API 서버 요청 없이 샘플로 로컬스토리지의 데이터를 받아오는 방식으로 작성중이다. 따라서 created 훅에서 실행시켜준다. created 훅은 Vue의 생명주기 중 하나이다. 
```javascript
<template>
    <div class="memo-app">
        <memo-form/>
        <!-- <memo/> -->
    </div>
</template>
<script>
import MemoForm from './MemoForm';

export default {
    name: 'MemoApp',
    components:{
        MemoForm
    },
    data(){
        return {
            memos: [],
        };
    },
    created(){
        // 1) 
        // 만약 localStorage 내에 memos 데이터가 있다면 
        //      created 훅에서 localStorage 의 데이터를 컴포넌트 내의 memos 내에 넣어주고
        // 아니면
        //      컴포넌트 내의 memos 를 비어있는 배열로 초기화 한다.  
        this.memos = localStorage.memos ? JSON.parse(localStorage.memos) : [];
    }
}
</script>
```
  
## MemoForm.vue
```javascript
<template>
    <div class="memo-form">
        <form>
            <fieldset>
                <div>
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
```

# 4. MemoForm - submit 이벤트 추가, submit 시 페이지 이동 방지 
- methods 속성내에 메서드로 addMemo() 함수를 추가해준다. 
- addMemo 함수에서는 this.$emit() 함수로 'addMemo'이벤트를 발생시키고,  
  사용자 입력데이터(title, content)를 MemoApp 컴포넌트에 emit을 이용해 전파한다.  
- Vue는 submit 이벤트가 발생할 때 개발자가 직접 event.preventDefault를 호출하지 않아도 되도록 prevent 옵션을 제공해준다. (5.1.4.5.2. v-on 장 참고)  

## MemoForm 템플릿 작성
템플릿에서는 form 태그에 대해 preventDefault 를 걸고, addMemo() 함수를 호출하도록 한다.

```html
<template>
    // ...
    <form @submit.prevent="addMemo">
        // ...
    </form>
</template>
```
그리고 addMemo()함수 내에서는 emit을 통해 이벤트를 전파한다. 
스크립트는 바로 아래의 "MemoForm 스크립트 작성"에서 정리한다.  

## MemoForm 스크립트 작성
resetFields(), addMemo() 함수 추가!! 자세한 내용은 아래 코드 참고
```javascript
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
```
  
# 5. MemoApp - MemoForm 컴포넌트로부터 전달받은 데이터를 로컬 스토리지에 추가
MemoApp 입장에서 MemoForm 컴포넌트는 자식컴포넌트이다. MemoForm 에서는 addMemo 이벤트를 부모인 MemoApp 에 전달하고 있다.  
여기서는, 전달받은 addMemo 이벤트를 처리하는 로직을 작성하는 과정을 정리한다.  

## MemoApp 스크립트 작성
MemoApp 컴포넌트 내의 script 영역 내에 addMemo(payload), storeMemo() 함수를 추가해주자.
```javascript
import MemoForm from './MemoForm';

export default {
    name: 'MemoApp',
    components:{
        MemoForm
    },
    // ... 
    methods: {
        // 템플릿의 <memo-form>에 addMemo 이벤트 콜백함수로 연결해줘야 한다. 
        // addMemo 이벤트는 자식 컴포넌트인 MemoForm 으로부터 전달받는다. 
        // (이벤트를 전달받으면서 payload도 함께 전달받는다)
        // 위의 template 코드 참고
        addMemo (payload){
            // MemoForm 에서 전달해주는 데이터를 먼저 컴포넌트 내부 데이터에 추가한다. 
            // (자식 컴포넌트인 MemoForm 에서 부모인 MemoApp 으로 데이터를 올려주는 것)
            this.memos.push(payload);
            
            // storeMemo() 호출
            this.storeMemo();
        },
        // 내부 데이터를 문자열로 변환하여, 로컬 스토리지에 저장한다.
        storeMemo (){
            const memosToString = JSON.stringify(this.memos);
            localStorage.setItem('memos', memosToString);
        }
    }
}
```
  
## MemoApp 템플릿 작성
이제 MemoApp에 정의한 함수인 addMemo() 를 호출해주는 곳이 필요하다. MemoApp에서 addMemo()를 호출하는 시점은 자식 컴포넌트인 MemoForm 으로부터 "addMemo" 이벤트를 전달받았을 때 이다. 템플릿 코드 내의 v-on 디렉티브로 MemoApp 에서도 addMemo 이벤트를 발생시키도록 하자.  

```html
<template>
    <div class="memo-app">
        <!-- <memo-form v-on::addMemo="addMemo"/> 과 같은 의미 -->
        <memo-form @addMemo="addMemo"/>
        <!-- <memo/> -->
    </div>
</template>
```

입력된 데이터를 Vue.js의 개발자 도구에서 확인하는 방법은
Application 탭 >> 좌측 사이드바 Storage 메뉴 >> 드랍다운 버튼 클릭 > http://... 로 나타나는 링크를 클릭하면 데이터의 상세 내용이 나타난다.  
localStorage로 개발하는 경우는 그리 많지 않으니 자세한 설명은 스킵!!하고 넘어간다. 

# 6. 메모 데이터 노출 기능 구현하기
먼저 Memo 컴포넌트를 작성해보자.  
  
## Memo 컴포넌트 작성
Memo 컴포넌트는 MemoApp에서 변수 memos를 v-for 디렉티브로 순회하며 Memo컴포넌트를 표현할 것이다. Memo 컴포넌트는 memos[i]에 대한 자식 컴포넌트이다. 참고로, MemoApp 컴포넌트는 현재 memos라는 메모 데이터를 localStorage에서 가져와 사용하고 있다. (localStorage 사용 코드는 추후 변경예정)  
  
부모 컴포넌트에서 자식 컴포넌트를 for문으로 돌릴 때 보통 props라는 개념을 통해 자식 컴포넌트에 데이터를 전달해준다. 주의할 점은 props는 읽기 전용이라는 점이다. react에서와 마찬가지로 props는 기본설정이 읽기 전용이다. vue.js에서는 자식 컴포넌트 내에서 props를 수정할 수는 있다. 하지만 권장하지는 않는 방법이다.  

props 를 전달하는 형식은 아래와 같다.
```html
<template>
    <!-- ... -->
    <ul class="memo-list">
        <memo v-for="memo in memos" :key="memo.id" :memo="memo"/>
    </ul>
</template>
```
- :key  
위 코드를 보면 key를 지정하고 있는데, key에 어느 곳에서도 중복되지 않는 고유한 식별값을 넣어주어야 에러가 나지 않는다.   (각 자식 컴포넌트를 구별하는 식별자 역할을 하므로)  
- :memo  
:memo라는 속성을 지정해주었는데, 이 속성은 자식 컴포넌트에 넘어가는 값이다.  
  

참고) v-for 디렉티브에 대해서는 2.1.5.5 v-for 를 참고하자.  

### Memo.vue
```html
<template>
    <li class="memo-item"></li>
</template>

<script>
export default {
    name: 'Memo',
}
</script>

<style scoped>

</style>
```
## MemoApp 컴포넌트에 Memo 컴포넌트 연동
참고로 새로운 컴포넌트를 부모 컴포넌트에 추가할 때마다 부주의하게 실수할 수 있는 부분이 있다.  
[참고](https://stackoverflow.com/questions/49154490/did-you-register-the-component-correctly-for-recursive-components-make-sure-to)
  
- 자식 컴포넌트에서 export default {name: "이름"}
- 부모 컴포넌트에서
    - import Memo from "./Memo";
    - export deffault { ... components: {MemoForm, Memo} }
와 같은 방식으로 추가해주어야 한다.  

### MemoApp 에 Memo 컴포넌트 등록
#### 템플릿 (MemoApp)
```html
<template>
    <div class="memo-app">
        <memo-form @addMemo="addMemo"/>
        <!-- 아래의 부분이 추가됨 -->
        <!-- 자식 컴포넌트인 Memo에서 li 태그로 각각의 메모를 표현한다. -->
        <ul class="memo-list">
            <memo v-for="memo in memos" :key="memo.id" :memo="memo"/>
        </ul>
    </div>
</template>
```
#### 스크립트 (MemoApp)
```javascript
// import 를 꼭 해주어야 한다.
import Memo from './Memo';
// ...
export default {
    ...
    // components: {... } 을 꼭 등록해주어야 한다.
    components:{
        ..., 
        Memo,   // 이 부분을 추가해준다.
                // 자식 컴포넌트를 부모 컴포넌트 내에 등록하는 과정이다.
    }
}
```

#### CSS (MemoApp)
```css
  .memo-list {
    padding: 20px 0;
    margin: 0;
  }
```

### Memo 컴포넌트 작성
이제 Memo 컴포넌트를 만들어보자. 아무것도 추가하지 않은 기본적인 구조는 아래와 같다.  
#### 기본적인 코드 구조
```html
<template>
    <li class="memo-item"></li>
</template>

<script>
export default {
    name: "Memo",
}
</script>

<style scoped>

</style>
```

#### props 로 부모 컴포넌트 데이터 연동
아직까지는 데이터가 컴포넌트로 보여지지 않을 것이다. 이유는 부모 컴포넌트로부터 데이터를 가져오지 않았기 때문이다. 부모 컴포넌트의 데이터를 자식 컴포넌트인 Memo와 연동하기 위해 props를 사용한다. 
```html
<template>
    <li class="memo-item">
        <strong>{{memo.title}}</strong>
        <p>{{memo.content}}</p>
        <button type="button">
            <i class="fas fa-times"></i>
        </button>
    </li>
</template>

<script>
export default {
    name: "Memo",
    // 이 부분이 추가 되었다. props로 memo 데이터를 추가
    props: {
        memo: {
            type: Object
        },
    }
}
</script>

<style scoped>
 /** 
  ...  */
</style>
```

템플릿 에서는 
```html
    <!-- ... -->
        <strong>{{memo.title}}</strong>
        <p>{{memo.content}}</p>
        <button type="button">
            <i class="fas fa-times"></i>
        </button>
    <!-- ... -->
```
을 추가하여 부모로부터 전달받은 memos[i] 데이터를 표시하도록 하고 있다.  
  
스크립트에서는 props 속성에 부모로부터 받아오는 속성을 명시적으로 지정해 받아온다.  
```javascript
<script>
export default {
    name: "Memo",
    // 이 부분을 추가했다.
    props: {
        memo: {
            type: Object
        },
    }
}
</script>
```

## 스타일 적용
여기까지 한 결과는 정상적으로 데이터를 불러오기는 하지만, CSS가 적용되어 있지 않아 다소 투박하다. 스타일을 적용해보자.
```css
<style scoped>
  .memo-item {
    overflow: hidden;
    position: relative;
    margin-bottom: 20px;
    padding: 24px;
    box-shadow: 0 4px 10px -4px rgba(0, 0, 0, 0.2);
    background-color: #fff;
    list-style: none;
  }
  .memo-item input[type="text"] {
    border: 1px solid #ececec;
    font-size: inherit;
  }
  .memo-item button {
    position: absolute;
    right: 20px;
    top: 20px;
    font-size: 20px;
    color: #e5e5e5;
    border: 0;
  }
  .memo-item strong {
    display: block;
    margin-bottom: 12px;
    font-size: 18px;
    font-weight: normal;
    word-break: break-all;
  }
  .memo-item p {
    margin: 0;
    font-size: 14px;
    line-height: 22px;
    color: #666;
  }
  .memo-item p input[type="text"] {
    box-sizing: border-box;
    width: 100%;
    font-size: inherit;
  }
  .memo-item p input[type="text"] {
    box-sizing: border-box;
    width: 100%;
    font-size: inherit;
  }
</style>
```

# 7. 메모 데이터 삭제 기능 구현
앞에서 우리는 MemoForm 컴포넌트의 props의 id로 new Date().getTime()을 지정하여 유일하게 식별할 수 있게해주었다. 이것은 각 자식 UI컴포넌트를 유일하게 식별할 수 있도록 하는 역할을 위해 지정했다. UID와 유사한 역할을 수행한다. 여기서는 이 UID를 이용하여 삭제로직을 구현한다.  

- Memo 컴포넌트
    - 템플릿  
        - @click 이벤트에 대해 deleteMemo() 함수를 호출하도록 명시한다.
    - 스크립트
        - deleteMemo() 메서드를 작성한다.
        - deleteMemo() 메서드에서는 
            - 현재 memo 컴포넌트의 id를 얻어오고
            - 'deleteMemo' 이벤트를 발생시킨다.
            - 이 'deleteMemo' 이벤트는 부모 컴포넌트인 MemoApp 컴포넌트로 전파된다.
- MemoApp 컴포넌트
    - 템플릿
        - \<memo\> 컴포넌트에 대해 @deleteMemo 이벤트에 대한 핸들러로 deleteMemo () 함수를 호출하도록 명시한다.
    - 스크립트
        - deleteMemo(id) 메서드를 작성한다.
        - deleteMemo(id) 내에서는 id를 기반으로 memo[i]의 인덱스를 찾는다.
        - 해당 memo[i]를 삭제한다.
        - 해당 내용을 저장한다.
  
> **참고)**  
> - Array.findIndex() 메서드
> - Array.splice() 메서드

## Memo 컴포넌트
```html
<template>
    <li class="memo-item">
        <!-- ... -->
        <button type="button" @click="deleteMemo">
            <i class="fas fa-times"></i>
        </button>
    </li>
</template>

<script>
export default {
    name: "Memo",
    props: {
        memo: {
            type: Object
        },
    },
    methods: {
        deleteMemo() {
            const id = this.memo.id;
            this.$emit('deleteMemo', id);
        }
    }
}
</script>
```
  
## MemoApp 컴포넌트
```html
<template>
    <div class="memo-app">
        <!-- ... -->
        <ul class="memo-list">
            <!-- props 로 memos[i]를 각각 전달해준다. -->
            <!-- deleteMemo 이벤트 : -->
            <!--    Memo 컴포넌트에서 올라오는 이벤트 이므로 @deleteMemo 이벤트 사용 -->
            <!--    @deleteMemo 이벤트에 대한 핸들러는 deleteMemo () 함수, 스크립트에 작성했다. -->
            <memo v-for="memo in memos" :key="memo.id" :memo="memo"
                  @deleteMemo="deleteMemo"/>
        </ul>
    </div>
</template>
<script>
import MemoForm from './MemoForm';
import Memo from './Memo';

export default {
    name: 'MemoApp',
    components:{
        MemoForm,
        Memo,
    },
    data(){
        return {
            memos: [],
        };
    },
    
    // ...
    // ...

    methods: {
        // ...
        // 내부 데이터를 문자열로 변환하여, 로컬 스토리지에 저장한다.
        storeMemo (){
            const memosToString = JSON.stringify(this.memos);
            localStorage.setItem('memos', memosToString);
        },
        // <memo> 컴포넌트로부터 id를 전달받아 삭제를 진행한다.
        deleteMemo (id){
            const indexOfDelete = this.memos.findIndex(_memo=>_memo.id===id);
            this.memos.splice(indexOfDelete, 1);
            this.storeMemo();
        }
    }
}
</script>
<style scoped>
/**
    // ...
*/
</style>
```

# 8. 메모 수정 기능
## 컴포넌트 기본 구조 작성 (1)
부모 컴포넌트인 MemoApp에서의 @updateMemo 이벤트 처리 구문과, Memo 컴포넌트의 기본화면을 작성한다. 현재 Memo 컴포넌트(자식)에는 input 필드 적용이 되어있지 않기 때문에 데이터를 수정할 수 없다. 이런 이유로 "컴포넌트 기본 구조 작성(1)"에서는 
- Memo 컴포넌트에 input 앨리먼트 적용, input 앨리먼트 CSS 적용
- MemoApp 컴포넌트에 자식 컴포넌트의 이벤트인 @updateMemo 이벤트/이벤트 핸들러 연결
을 작성한다.  
### MemoApp 컴포넌트 (부모)
템플릿 에서는 @updateMemo 이벤트에 대해 updateMemo()함수를 호출하도록 작성한다.
```html
<template>
    ...
    <ul class="memo-list">
        <memo v-for="memo in memos" :key="memo.id" :memo="memo"
              @deleteMemo="deleteMemo"
              @updateMemo="updateMemo"/>
    </ul>
    ...
</template>
```
  
스크립트 에서는 updateMemo() 함수를 작성한다. 로직을 간단히 설명하자면, 
- 자식컴포넌트(Memo)에서 update하려는 컨텐트의 id, content를 비구조화 할당으로 얻어낸다. (수정하려는 데이터의 id, content)
- this.memos에서 해당 데이터의 index와 memo 객체를 얻어낸다. (원본 데이터)
- this.memos의 해당 인덱스의 원본 데이터에서 content만 싹 골라서 수정한다. (비구조화 할당 사용 ...연산자)

```javascript
<script>
    export default {
        // ... 
        methods: {
            updateMemo (payload){
                const {id, content} = payload;
                const indexOfUpdate = this.memos.findIndex(_memo => _memo.id === id;);
                const objOfUpdate = this.memos[indexOfUpdate];

                this.memos.splice(indexOfUpdate, 1, {...objOfUpdate, content});
                this.storeMemo();
            }
        }
    }
</script>
```
### Memo 컴포넌트 (자식)
html 내에 input 태그를 아래와 같이 넣어주자. 
- 화면 노출 시에는
    - \<p\> 태그를 보여주고
- 더블클릭하여 수정하려 할때는 
    - input 태그를 보여주기 위한 기본 구조이다.
```html
<template>
    <li class="memo-item">
        <strong>{{memo.title}}</strong>
        <p @dblclick="handleDbClick">
          <template>{{memo.content}}</template>
          <input type="text" ref="content" :value="memo.content"/>
        </p>
        <button type="button" @click="deleteMemo">
          <i class="fas fa-times"></i>
        </button>
    </li>
</template>
```

## 컴포넌트 기본구조 작성 (2) - @dblClick 이벤트 연결
아래의 요구사항을 boolean 조건값으로 제어하는 로직을 작성한다.
> - 화면 노출 시에는
>    - \<p\> 태그를 보여주고
> - 더블클릭하여 수정하려 할때는 
>    - input 태그를 보여준다. 

위의 두 가지 (p태그를 보여주거나, input 태그를 보여주는)를 boolean 조건값에 따라 다르게 동작하도록 해야 한다. 여기서는 일단 더블클릭 한번에 input 태그로 전환하는 기능을 작성한다.  

### 템플릿 (Memo.vue)
isEditing === true 일 때에는 메모 내용만을 보여주고  
isEditing === false 일 때에는 해당 영역을 input 태그로 전환되도록 한다.  
v-if, v-else 에 따라 보여줄 html 태그를 선택하게끔 해준다.  
  
- v-if 에서는
    - !isEditing, 즉, isEditing 이 false 일 때에만 memo.content 를 보여주도록 해준다.
- v-else 에서는
    - isEditing 이 true 일때 input 태그를 보여준다.

```html
<template>
    <li class="memo-item">
        <strong>{{memo.title}}</strong>
        <p @dblclick="handleDblClick">
          <template v-if="!isEditing">{{memo.content}}</template>
          <input v-else type="text" ref="content" :value="memo.content"/>
        </p>
        <button type="button" @click="deleteMemo">
          <i class="fas fa-times"></i>
        </button>
    </li>
</template>
```
  
### 스크립트 (Memo.vue)
스크립트에서는 handleDblClick() 함수를 작성했다. 더블 클릭시에는 멤버필드인 isEditing 을 true로 초기화 해준다. isEditing 을 true 로 초기화 한 후에는, input 태그에 focus가 맞춰주도록 코드를 구현했다.  
    
그리고, Memo 컴포넌트 내에서 사용될 멤버필드와 같은 역할을 할 data 속성에 isEditing 을 지정해준다.  
(멤버 필드로 지칭하는 것은 조금 어폐가 있지만... 일단은 멤버필드라고 지칭하자... )  
  
```javascript
export default {
    name: "Memo",
    data() {
        return {
            isEditing: false,
        }
    },
    // ...
    methods: {
        // ...
        handleDblClick(){
          this.isEditing = true;
          this.$refs.content.focus();
        }
    }
}
```
  
## 컴포넌트 기본구조 작성 (3) - 렌더링 시점과 이벤트 동기화
위 코드 까지는 메모 영역을 더블클릭하면 input 필드로 변경되지만, 개발자 도구 내에서는 아래의 에러가 발생한다.  
  
> [Vue warn]: Error in v-on handler: "TypeError: Cannot read property 'focus' of undefined"  
>  
> found in  
>  
> ---> <Memo> at src/components/Memo.vue  
>       <MemoApp> at src/components/MemoApp.vue  
>         <App> at src/App.vue  
>           <Root>  

이렇게 되는 원인을 결론만 놓고 보면  
> **"렌더링을 새로 하는 시점에 this.$refs.content.focus() 를 하게 되므로 해당 요소가 다 그려지지 않은 시점에 this.$refs.content.focus()를 하고 있어서"**  
이다. 

```javascript
handleDblClick(){
    this.isEditing = true;
    this.$refs.content.focus();
}
```  

Memo 컴포넌트에서 더블클릭 이벤트가 발생할 때 우리는 컴포넌트 내의 데이터 isEditing을 수정하도록 로직을 작성했다. 컴포넌트 내에서 데이터의 값이 변경되면 다시 렌더링된다. 그리고 this.$refs.content.focus()를 실행하고 있다.  

handleDblClick(){...} 내부를 자세히 보면 렌더링이 끝난 후에 focus()가 되도록 
- this.isEditing = true;
- this.$refs.content.focus();
를 차례대로 호출하고 있음에도 불구하고 this.$refs.content 는 undefined 가 되어 에러를 낸다.  
  
이런 현상은 Memo 컴포넌트의 isEditing 데이터가 변경되는 시점에
- Memo 컴포넌트의 this.$refs.content.focus() 함수 실행
- Memo 컴포넌트 리렌더링 시점
이 서로 다른 순서로 실행되기 때문이다. 
  
data변경시점의 리렌더링 작업과 UI조작 작업이 순서대로 흐르지 않는 이유는... Vue의 컴포넌트가 리렌더링 되는 작업의 흐름은 handleDblClick()이 실행되는 흐름과 별개로 실행되기 때문이다. 즉, **리렌더링 작업은 비동기 작업으로 따로 수행**되므로 리렌더링시 ui의 엘리먼트들이 모두 마운트 되는 이벤트 내에서 수행하도록 해주는 것이 옳다.  

### 테스트를 해보자
```javascript
export default {
    // ...
    beforeUpdate() {
        console.log("beforeUpdate :: ", this.$refs.content);
    },
    updated(){
        console.log("updated :: ", this.$refs.content);
    },
    // ...
    methods: {
        // ...
        handleDblClick(){
            this.isEditing = true;
            console.log("handleDblClick :: ", this.$refs.content);
            this.$refs.content.focus();
        }
    }
}
```
  
![이미자](./img/BEFORE_UPDATE_UPDATED.png)
첨부된 결과화면을 보면 
- beforeUpdate 시점에는 ui가 생성되지 않았다.  
- updated 시점에는 ui가 생성되어 있으며 ui 앨리먼트를 셀렉트 가능하다.  

updated 함수에서 $refs.content.focus()를 실행하면 될 것 같다. 하지만, 이렇게 할 경우 동작은 하지만 updated() 에서 수행된다. 즉, 이벤트가 발생했을 때가 아니라 컴퍼넌트가 새로 그려지는 시점에 포커스를 맞추게 된다.  
  
이런 이유로 이벤트 핸들러인 handleDblClick() 메서드에서 ui를 새로 그리는 작업이 마무리된 시점을 잡아채서 그 시점에 ui 조작 로직인 this.$refs.content.focus() 를 수행해야 한다. 이때 사용되는 함수가 $nextTick() 함수이다.  

즉, 이벤트 핸들러 handleDblClick()에서 컴포넌트의 리렌더링 작업이 마무리된 시점에 해당 시점을 가로채어 수행되도록 보장하려면 $nextTick을 사용하면 된다.
  
예제)  
```javascript
export default {
    // ...
    methods : {
        name: "Memo",
        data(){
            return {
                isEditing: false,
            }
        }
        // ...
        handleDblClick(){
            this.isEditing = true;
            this.$nextTick(() => {
                this.$refs.content.focus();
            });
        }
    }
}
```
  
### 참고) nextTick
$nextTick 메서드는 다음 렌더링 사이클 이후 실행될 콜백함수를 등록할 수 있는 기능을 제공하는 메소드이다.  
  
그리고 "Tick"은 Vue가 상태를 갱신한 후 갱신된 상태를 기반으로 화면을 다시 그리는 주기를 Tick이라고 한다.  
  
## 컴포넌트 기본구조 작성 (4) - 메모 수정 (Memo 컴포넌트)
부모 컴포넌트인 MemoApp.vue 에서 @updateMemo 이벤트에 대한 핸들러를 등록했다. 이제 자식 컴포넌트인 Memo.vue에서 부모컴포넌트로 이벤트와 payload를 전파하는 로직을 작성하자.  

### 템플릿
템플릿에서는 input 태그에 
- @keydown.enter 이벤트에 대한 핸들러 호출로직 추가
    - @keydown.enter="updateMemo"  
- @bluer 이벤트에 대한 핸들러 호출로직 추가
    - @blur="handleBlur"

@keydown.enter 이벤트는 키보드가 눌릴때 그중 엔터키가 눌릴때의 이벤트이며, vue.js에서 제공하는 이벤트이다. 개인적으로... 이벤트 이름도 뭔가 직관적이어서 마음에 든다...ㅋㅋ  
  
@blur 이벤트는 커서가 현재 컴포넌트에서 사라지는 이벤트를 의미한다. vue.js에서만 제공하는 이벤트인지는 찾아봐야 알 것 같다.  
  
```html
<template>
    <li class="memo-item">
        <!-- ... -->
          <template v-if="!isEditing">{{memo.content}}</template>
          <input v-else type="text" ref="content" :value="memo.content"
                 @keydown.enter="updateMemo"
                 @blur="handleBlur"/>
        </p>
        <!-- ... -->
    </li>
</template>
```

### 스크립트
메모컴포넌트 내의 
- @updateMemo 이벤트에 대한 핸들러 로직
    - 이벤트가 발생했을 때 input 태그의 텍스트 값을 가져오고
    - 텍스트에 아무값도 없는지 체크하는 등의 유효성 체크 후
    - 부모 컴포넌트로 this.$emit("updateMemo", {id,content})를 통한 이벤트를 전파하고 있다.
    - 이벤트 전파를 완료한 후에는 isEditing 플래그를 false로 세팅한다.
- @blur 이벤트에 대한 핸들러 로직
    - 커서가 메모 컴포넌트 바깥으로 사라졌을 때의 로직이다.
    - isEditing 플래그를 false로 세팅한다.  
  
```javascript
export default {
    name: "Memo",
    data(){
        return {
            isEditing: false,
        }
    },
    props: {
        memo: {
            type: Object,
        }
    },
    methods :{
        updateMemo(e){
          const id = this.memo.id;
          const content = e.target.value.trim();
          
          if(content.length <=0){
            return false;
          }

          // "updateMemo" 이벤트를 데이터 {id,content}와 함께 부모 컴포넌트로 전파 
          this.$emit('updateMemo', {id, content});
          // update가 완료된 후에는 수정가능 여부 플래그를 false 로 세팅
          this.isEditing = false;
        },
        // ...
        // ...
        hadnleBlur(){
          this.isEditing = false;
        }
    }
}
```

# 9. 메모 카운트 기능 (props 활용)
여기서는 메모의 갯수를 카운트하는 기능을 작성해보려고 한다. 카운트 기능을 단순히 props를 이용한 부모/자식 사이의 상태관리로 구현해보려 한다. 이 기능은 추후 vuex를 적용해 관리할 예정이다. count 변수는 단순히 읽기만 가능해서는 안되기 때문에 vuex를 이용한 상태관리가 필요하다. memo-app-work-steps-2.md 에서부터 vuex 활용 예제를 정리할 예정이다. 여기서는 일단 props를 활용한 부모/자식 사이의 상태관리 예제를 정리한다.  
  
TMI 이다..... 필요없는 내용은 지우자~ 정리 필요!!!  
- App 컴포넌트(App.vue)의 자식 컴포넌트인 MemoApp 컴포넌트에서 메모의 추가/삭제가 있을 경우 App 컴포넌트로 @change 이벤트를 전파하고, 
- AppHeader는 App 컴포넌트(App.vue) 내에서 props 변수 memoCount 가 변할 때마다 화면에 해당 내용을 반영해주어야 한다.  
  

### MemoApp.vue
- addMemo
- deleteMemo
시에 이벤트 'change'를 부모 컴포넌트인 App.vue로 전송하는 역할을 한다.
```javascript
export default {
    name: 'MemoApp',
    components:{
        MemoForm,
        Memo
    },
    data(){
        return {
            memos: [],
        };
    },
    // ...
    methods: {
        // ...
        addMemo(payload){
            // ...
            this.$emit('change', this.memos.length);
        },
        deleteMemo(id){
            // ...
            this.$emit('change', this.memos.length);
        }
    }
}
```

### App.vue

#### 템플릿
- app-header 컴포넌트에서는 
    - memoCount 변수를 :memoCount="memoCount" 로 주어 props로 전달해준다.
    - App.vue -> AppHeader.vue 로의 props 전달 흐름이다.
- memo-app 컴포넌트의 템플릿에서는 
    - @change 이벤트에 대해 "updateCount"함수를 호출하도록 해준다.
    - MemoApp.vue -> App.vue 로의 이벤트 전파가 이루어진다.
    - updateCount 함수에서는 data 영역에 선언된 변수인 memoCount를 수정한다.

```html
<template>
  <div id="app">
    <app-header :memoCount="memoCount"/>
    <memo-app @change="updateCount"/>
  </div>
</template>
<script>
import AppHeader from './components/AppHeader';
import MemoApp from './components/MemoApp';

export default{
    name: 'app',
    components:{
        AppHeader,
        MemoApp,
    },
    data(){
        return{
            memoCount: 0,
        }
    },
    methods: {
        updateCount(count){
            this.memoCount = count;
        }
    }
}
</script>
// ...
```

### AppHeader.vue
memoCoun는를 부모인 App.vue 로부터 전달받기 때문에 memoCount는 props 내에 선언해주어야 한다.  
부모로부터 전달 받은 memoCount를 그대로 \<template\> 내의 p 태그 안에 바인딩 해주었다.

```html
<template>
    <div class="app-header">
        <h1>메모 애플리케이션</h1>
        <p>{{memoCount}}</p>
    </div>
</template>

<script>
export default {
    name: 'AppHeader',
    props:{
        memoCount : {
            type: Number,
            default: 0,
        }
    }
}
</script>

<style scoped>
    .app-header {
        overflow: hidden;
        padding: 52px 0 27px;
    }

    .app-header h1{
        float: left;
        font-size: 24px;
        text-align: center;
    }
</style>
```


### 단점
메모 폼 데이터 수정, 추가 등에서는 그나마 복잡하지 않았다. 그런데 부모와 연관된 형제 컴포넌트를 수정하면서 연관관계가 조금씩 복잡해졌다. 실제 업무로 진행하게 되면 관리 포인트가 많아지기 때문에 조금 불편해질 수도 있다는 생각이 들었다. 많이 알고서 일일이 수정하는 것보다 효율적으로 한방에 고칠수 있는 코드구조가 필요하다는 생각이다.  

바로 요렇게 여러가지 컴포넌트가 얽혀 있을 때 상태관리로 사용하는 것이 vuex이다. redux를 계승했지만, vue.js 에 특화되어 있는 상태관리 컴포넌트이다. redux는 react관련 해서 대중적으로 사랑을 받은 컴포넌트로 알려져 있지만, 일반 javascript에서도 import해서 쓸수도 있다. react 전용이 아닌 범용이다. react에서도 기본 redux사용법과 조금 달라졌던걸로 기억한다.. vuex역시 기본 redux코드와 조금 달라진다.  
  
memo-app-work-steps-2.md 에서
- vuex 적용 예제
- axios 커스터마이징 등의 활용
을 정리할 예정이다.
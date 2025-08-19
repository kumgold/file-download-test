# Widgets

## Stateless Widget
- 상태변화가 없는 위젯. 주로 많은 위젯이 StatelessWidget으로 구현된다. (예: Text, Icon 등)
- build 메서드를 반드시 오버라이드 해야 한다. build 메서드는 Widget을 그리고 Widget Tree에 추가하는 역할을 하는 메서드이다.
- build 메서드는 Widget을 생성하거나 Widget의 Dependency(예를 들면, Widget에 전달된 State)가 변경될 때 호출된다.
- 모든 프레임에서 호출될 수 있기 때문에 어떤 Side Effect를 포함해서는 안된다.
```dart
class PaddedText extends StatelessWidget {
  const PaddedText({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: const Text('Hello, World!'),
    );
  }
}
```

## Stateful Widget
- 유저 Input에 따라 상태가 변하는 Widget은 Stateful Widget으로 구현한다.
- StatefulWidget은 build 메서드를 포함하지 않는다. State라는 Subclass에 변경 가능한 상태를 저장하여 표현한다.
- State Subclass에 build 메서드가 포함되어 있다. State 객체에 변경이 발생되었다면 반드시 setState를 호출해서 Framework에 변경되었다는 신호를 보내야 한다.
- Widget으로부터 State를 분리한다면 다른 Widget의 상태 손실 걱정 없이 StatelessWidget과 StatefulWidget을 같은 방식으로 처리할 수 있다.
```dart
 class CounterWidget extends StatefulWidget {
  @override
  State<CounterWidget> createState() => _CounterWidgetState();
}

class _CounterWidgetState extends State<CounterWidget> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }
  
  @override
  Widget build(BuildContext context) {
    return Text('$_counter');
  }
}
```

<hr/>
참고 자료 : 
https://docs.flutter.dev/get-started/fundamentals/state-management <br/>
https://docs.flutter.dev/get-started/fundamentals/widgets

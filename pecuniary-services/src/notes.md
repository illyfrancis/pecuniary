# Views

the opinion is that for the purpose of navigation, a router should be used instead of mediator.

### why?

Mediator was introduced as a way of event aggregator or to provide event bus capability. Use of `event` for 'displaying' a view or 'navigating' to a view doesn't seem like a proper application. Wanted a separate and more specialized component that can manage the navigation concerns and the router  along with HTML5 history support fulfills the need. Nice side-effect is the bookmarkable page URL we get from using the router with history api.

### how?

Define a router with router hash that maps URL fragments to functions. In each function call view.render etc Uncertain if it should also create the view object or not. See below.

### questions or concerns

Unsure about how to structure the views and router(s). Perhaps separate out routers per logical group so end up with multiple routers. The dependencies has to be managed within router to views but requirejs should take care of this so no concern there.

- should the creation of view be done within router? is it the responsibility of router?
- often time view creation has dependencies on underlying data, again who should be managing the dependencies?

For both of these question I don't believe it is the router's responsibility but rather it should be managed external to it. Then the question becomes who takes care of it? Should we continue with the idea of view factory? and global repository object?

Who manage the lifecycle of a view object? Should the view be destroyed when hidden?

# Repository

In previous implementation, certain underlying datasets were pre-loaded via `loadAll` but is that necessary or can it be avoided?

Looking back one of the reason for pre-fetching 'all' collections were to support the view creation. It is simpler to assume that the required data is available by the time view is rendered but this approach may not be the most optimal. Can we achieve the data fetch only when needed? Or are there just too many reference/global data it makes no sense to interactively load the data?

# Different approach

Let's think through how above can be done. That is, only load data when needed i.e. when the view is created and need to consume the data for some purpose (e.g. render, calculate etc).

### Use case: display payee list

Not considering pagination of a long list etc, we need a collection of payee and a view to render the list. 

The flow is:

1. navigate (either via script or URL) to `/payees`
2. router maps the request to a function that 
	- gets payees view
	- gets the list of payees via collections fetch or load etc
	- when collections loaded the view renders (i.e. view listens for events from collection)


### Using ViewManager (not so much ViewFactory)

Difference between the factory and the manager is that while factory only concerns with the creation the manager should manage the lifecycle of view object. Such that, VM.getPayees() should create the view if one doesn't exist before handing out the payees view. If one existed it would just give the same view it had created before. 

This might mean VM need to supply disposeView(name) function that properly disposes of the view, that is not only call "view".dispose() but also remove the internal reference to it so that it can be re-created when necessary.

Back to the creation question, whereby the constructor of the view may require data, how should that be passed into it?

In ViewFactory that was hidden from the caller but now I think it should be completely exposed and allow the full option to be passed in.

Thinking something like:

```
```

# Kinda global object

Things like view manager, repository, event bus (mediator) etc are needed by many other objects. Put it in a global like object and pass it around?

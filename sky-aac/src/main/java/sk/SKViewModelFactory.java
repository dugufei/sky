package sk;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午9:16
 * @see SKViewModelFactory
 */
public class SKViewModelFactory implements ViewModelProvider.Factory {
	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		return null;
	}

//	private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;
//
//	@Inject
//	public SKViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
//		this.creators = creators;
//	}
//
//	@SuppressWarnings("unchecked") @Override public <T extends ViewModel> T create(Class<T> modelClass) {
//		Provider<? extends ViewModel> creator = creators.get(modelClass);
//		if (creator == null) {
//			for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
//				if (modelClass.isAssignableFrom(entry.getKey())) {
//					creator = entry.getValue();
//				 	break;
//				}
//			}
//		}
//		if (creator == null) {
//			throw new IllegalArgumentException("unknown model class " + modelClass);
//		}
//		try {
//			return (T) creator.get();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
}
